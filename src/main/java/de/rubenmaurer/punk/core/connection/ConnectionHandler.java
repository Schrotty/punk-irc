package de.rubenmaurer.punk.core.connection;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import de.rubenmaurer.punk.core.PunkServer;

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
                .match(String.class, s -> {
                    System.out.println("SERVER: " + s.intern() + "\r\n");
                    remote.tell(TcpMessage.write(ByteString.fromString(s.intern() + '\r' + '\n'), TcpMessage.noAck()), getSelf());
                })
                .build();
    }
}