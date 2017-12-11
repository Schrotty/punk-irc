package de.rubenmaurer.punk.core.irc.parser;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import de.rubenmaurer.punk.IRCLexer;
import de.rubenmaurer.punk.IRCListener;
import de.rubenmaurer.punk.IRCParser;
import de.rubenmaurer.punk.core.Guardian;
import de.rubenmaurer.punk.core.irc.client.Connection;
import de.rubenmaurer.punk.core.reporter.Report;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Arrays;

/**
 * Actor for parsing incoming messages.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Worker extends AbstractActor {

    private ActorRef handler;

    /**
     * Get props for creating a new actor.
     *
     * @return the props
     */
    public static Props props() {
        return Props.create(Worker.class);
    }

    /**
     * Parse a message.
     *
     * @param connection the connection to parse for
     * @param message the message to parse
     */
    private void parse(Connection connection, String message) {
        CharStream stream = CharStreams.fromString(message);

        IRCLexer lexer = new IRCLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        IRCParser parser = new IRCParser(tokens);

        IRCParser.ChatLineContext context = parser.chatLine();

        ParseTreeWalker walker = new ParseTreeWalker();
        IRCListener listener = new CommandListener(connection, self());

        walker.walk(listener, context);
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * Handles incoming messages and process them.
     *
     * @return a receiveBuilder object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, msg -> {
                    handler = msg.getConnection().getConnection();
                    Arrays.stream(msg.getMessage().split("\r\n")).forEach(arg -> parse(msg.getConnection(), arg));
                })
                .match(Connection.class, connection -> {
                    handler.tell(connection, self());
                    context().parent().tell(self().path().name(), self());
                })
                .build();
    }
}
