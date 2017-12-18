package de.rubenmaurer.punk.core.irc;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import de.rubenmaurer.punk.core.Guardian;
import de.rubenmaurer.punk.core.irc.channel.ChannelManager;
import de.rubenmaurer.punk.core.irc.client.ConnectionManager;
import de.rubenmaurer.punk.core.irc.messages.Info;
import de.rubenmaurer.punk.core.irc.parser.ParserManager;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Template;

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
     * The used channel manager actor.
     */
    private static ActorRef channelManager;

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
     * Get the channel-manager.
     *
     * @return the channel manager
     */
    public static ActorRef getChannelManager() {
        return channelManager;
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
        Props conProps = ConnectionManager.props(Tcp.get(getContext().getSystem()).manager());

        Guardian.reporter().tell(Report.create(Report.Type.ONLINE), self());
        connectionManager = context().actorOf(conProps, "connection-manager");
        channelManager = context().actorOf(ChannelManager.props(),"channel-manager");
        parser = context().actorOf(ParserManager.props(), "parser-manager");

        parser.tell(Template.get("parserWorkStart").toString(), self());
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
        return receiveBuilder()
                .match(Info.READY.getClass(), s -> context().parent().tell(Info.READY, self()))
                .build();
    }
}
