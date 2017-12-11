package de.rubenmaurer.punk.core.irc;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import de.rubenmaurer.punk.core.Guardian;
import de.rubenmaurer.punk.core.irc.client.ConnectionManager;
import de.rubenmaurer.punk.core.irc.parser.Parser;
import de.rubenmaurer.punk.core.reporter.Report;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Actor for managing all parts for the irc-server.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class PunkServer extends AbstractActor {

    /**
     * The used connection manager actor.
     */
    private static ActorRef connectionManager;

    /**
     * The used parser actor.
     */
    private static ActorRef parser;

    /**
     * Get the parser.
     *
     * @return the parser
     */
    public static ActorRef getParser() {
        return parser;
    }

    /**
     * Get the connection-manager.
     *
     * @return the connection manager
     */
    public static ActorRef getConnectionManager() {
        return connectionManager;
    }

    /**
     * Get props for creating a new actor.
     *
     * @return the props
     */
    public static Props props() {
        return Props.create(PunkServer.class);
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        parser = context().actorOf(Parser.props(), "parser");
        connectionManager = context().actorOf(
                ConnectionManager.props(Tcp.get(getContext().getSystem()).manager()), "connection-manager");

        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * Gets fired after stop.
     */
    @Override
    public void postStop() {

    }

    /**
     * Handles incoming messages and process them.
     *
     * @return a receiveBuilder object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
