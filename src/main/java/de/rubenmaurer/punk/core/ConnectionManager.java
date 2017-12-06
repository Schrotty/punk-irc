package de.rubenmaurer.punk.core;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.Tcp.Bound;
import akka.io.Tcp.CommandFailed;
import akka.io.Tcp.Connected;
import akka.io.TcpMessage;
import de.rubenmaurer.punk.core.handler.ConnectionHandler;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Settings;

import java.net.InetSocketAddress;

/**
 * The TCP ConnectionManager for handling incoming connections.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class ConnectionManager extends AbstractActor {

    /**
     * The used TCP manager.
     */
    private final ActorRef manager;

    /**
     * Constructor for a new server.
     *
     * @param manager the used TCP manager
     */
    public ConnectionManager(ActorRef manager) {
        this.manager = manager;
    }

    /**
     * Get the props needed for creating a new actor.
     *
     * @param manager the needed TCP manager
     * @return the props
     */
    public static Props props(ActorRef manager) {
        return Props.create(ConnectionManager.class, manager);
    }

    /**
     * Gets fired before start.
     */
    @Override
    public void preStart() {
        final ActorRef tcp = Tcp.get(getContext().getSystem()).manager();
        tcp.tell(TcpMessage.bind(getSelf(),
                new InetSocketAddress("localhost", Settings.port()), 100), getSelf());

        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * Receives messages and process them.
     *
     * @return a Receive object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Bound.class, msg -> manager.tell(msg, getSelf()))
                .match(CommandFailed.class, msg -> getContext().stop(getSelf()))
                .match(Connected.class, conn -> {
                    manager.tell(conn, getSelf());
                    final ActorRef handler = getContext().actorOf(
                            Props.create(ConnectionHandler.class));
                    getSender().tell(TcpMessage.register(handler), getSelf());
                })
                .build();
    }
}
