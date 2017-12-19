package de.rubenmaurer.punk.core.irc.parser;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import de.rubenmaurer.punk.IRCLexer;
import de.rubenmaurer.punk.IRCListener;
import de.rubenmaurer.punk.IRCParser;
import de.rubenmaurer.punk.core.Guardian;
import de.rubenmaurer.punk.core.irc.messages.impl.ParseMessage;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Template;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.Arrays;

/**
 * Actor for parsing incoming messages.
 *
 * @author Ruben Maurer
 * @version 1.2
 * @since 1.0
 */
public class ParserWorker extends AbstractActor {

    /**
     * Get props for creating a new actor.
     *
     * @return the props
     */
    public static Props props() {
        return Props.create(ParserWorker.class);
    }

    /**
     * Parse a message.
     *
     * @param message the message to parse
     */
    private void parse(String message) {
        CharStream stream = CharStreams.fromString(message);

        IRCLexer lexer = new IRCLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        IRCParser parser = new IRCParser(tokens);

        IRCParser.ChatLineContext context = parser.chatLine();

        ParseTreeWalker walker = new ParseTreeWalker();
        IRCListener listener = new CommandListener(sender(), self());

        walker.walk(listener, context);
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
        context().parent().tell(Template.get("subSysReady").toString(), self());
    }

    /**
     * Handles incoming messages and process them.
     *
     * @return a receiveBuilder object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ParseMessage.class, msg -> {
                    Arrays.stream(msg.getMessage().split("\r\n")).forEach(this::parse);
                })
                .matchEquals(Template.get("workerDone").toString(), s -> context().parent().tell(self().path().name(), self()))
                .build();
    }
}
