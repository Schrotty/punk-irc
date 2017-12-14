package de.rubenmaurer.punk.core.irc.client;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

import de.rubenmaurer.punk.core.irc.PunkServer;
import de.rubenmaurer.punk.core.irc.parser.Message;
import de.rubenmaurer.punk.core.reporter.Report;

import static de.rubenmaurer.punk.core.Guardian.reporter;

/**
 * Actor for handling a established connection.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class ConnectionHandler extends AbstractActor {

    /**
     * The remote endpoint actor.
     */
    private ActorRef remote;

    /**
     * The used connection object for storing data.
     */
    private Connection connection;

    /**
     * Get needed props for creating a new actor.
     *
     * @return the props for creating
     */
    public static Props props() {
        return Props.create(ConnectionHandler.class);
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * Gets fired after stop.
     */
    @Override
    public void postStop() {
        reporter().tell(Report.create(Report.Type.OFFLINE), self());
    }

    /**
     * Handles incoming messages and process them.
     *
     * @return a receiveBuilder object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Received.class, msg -> {
                    if (connection == null && remote == null) {
                        remote = getSender();
                        connection = Connection.create(getSelf());
                        ConnectionManager.connections.add(connection);
                    }

                    System.out.println(msg.data().decodeString("US-ASCII"));
                    PunkServer.getParser().tell(Message.create(msg.data().decodeString("US-ASCII"), connection), self());
                })
                .match(Tcp.ConnectionClosed.class, msg -> getContext().stop(getSelf()))
                .match(String.class, s -> remote.tell(TcpMessage.write(ByteString.fromString(s.intern() + '\r' + '\n'), TcpMessage.noAck()), getSelf()))
                .match(Connection.class, con -> {
                    int index = ConnectionManager.connections.indexOf(connection);
                    if (index != -1) ConnectionManager.connections.set(index, con);

                    connection = con;
                })
                .match(de.rubenmaurer.punk.core.irc.client.Message.class, s -> s.getTarget().tell(s.toString(), self()))
                .match(de.rubenmaurer.punk.core.irc.messages.WhoIs.class, msg -> {
                    if (msg.isRequest()) context().parent().tell(msg, self());
                    if (!msg.isRequest()) connection.finishRequest(msg);
                })
                .build();
    }
}