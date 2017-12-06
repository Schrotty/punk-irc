package de.rubenmaurer.punk.core.handler;

import akka.actor.AbstractActor;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import de.rubenmaurer.punk.IRCLexer;
import de.rubenmaurer.punk.IRCListener;
import de.rubenmaurer.punk.IRCParser;
import de.rubenmaurer.punk.core.listener.CommandListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * A handler for incoming TCP connections.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class ConnectionHandler extends AbstractActor {

    private IRCParser parser;

    private void conn(String value) {
        CharStream input = CharStreams.fromString(value);

        IRCLexer lexer = new IRCLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        IRCParser parser = new IRCParser(tokens);

        // Specify our entry point
        IRCParser.ChatContext context = parser.chat();

        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        IRCListener listener = new CommandListener();

        walker.walk(listener, context);
    }

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
                    //System.out.println(data.decodeString("US-ASCII"));
                    conn(data.decodeString("US-ASCII"));
                    getSender().tell(TcpMessage.write(data), getSelf());
                })
                .match(Tcp.ConnectionClosed.class, msg -> getContext().stop(getSelf()))
                .build();
    }
}