package de.rubenmaurer.punk.core.irc.client;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import de.rubenmaurer.punk.core.Guardian;
import de.rubenmaurer.punk.core.reporter.Report;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * Actor for managing all established connections.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class ConnectionManager extends AbstractActor {

    /**
     * The used tcp-manager.
     */
    private final ActorRef manager;

    /**
     * List of all established connections.
     */
    static List<Connection> connections = new LinkedList<>();

    /**
     * Create a new connection-manager object.
     *
     * @param manager the tcp-manager to use
     */
    public ConnectionManager(ActorRef manager) {
        this.manager = manager;
    }

    /**
     * Get the props needed for creating a new actor.
     *
     * @param manager the tcp-manager to use
     * @return the props for creating new actor
     */
    public static Props props(ActorRef manager) {
        return Props.create(ConnectionManager.class, manager);
    }

    /**
     * Server has connection with given nickname?
     *
     * @param nickname the given nickname
     * @return has connection with nickname?
     */
    public static boolean hasNickname(String nickname) {
        for (Connection connection: connections) {
            if (connection.getNickname().equals(nickname)) return true;
        }

        return false;
    }

    /**
     * Gets fired before actor startup.
     */
    @Override
    public void preStart() {
        final ActorRef tcp = Tcp.get(getContext().getSystem()).manager();
        tcp.tell(TcpMessage.bind(getSelf(), new InetSocketAddress("localhost", 6667), 100), getSelf());

        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * Handles incoming messages and process them.
     *
     * @return a receiveBuilder object
     */
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Bound.class, msg -> manager.tell(msg, getSelf()))
                .match(Tcp.CommandFailed.class, msg -> getContext().stop(getSelf()))
                .match(Tcp.Connected.class, conn -> {
                    manager.tell(conn, getSelf());
                    getSender().tell(TcpMessage.register(getContext().actorOf(ConnectionHandler.props())), getSelf());
                })
                .build();
    }
}
