package de.rubenmaurer.punk.core.handler;

import akka.actor.AbstractActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;

/**
 * A handler for incoming TCP connections.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class ConnectionHandler extends AbstractActor {

    /**
     * Receives messages and process them.
     *
     * @return a Receive object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Tcp.Received.class, msg -> {
                    final ByteString data = msg.data();
                    System.out.println(data.decodeString("US-ASCII"));
                    getSender().tell(TcpMessage.write(data), getSelf());
                })
                .match(Tcp.ConnectionClosed.class, msg -> getContext().stop(getSelf()))
                .build();
    }
}