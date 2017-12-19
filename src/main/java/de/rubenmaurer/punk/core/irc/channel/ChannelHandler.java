package de.rubenmaurer.punk.core.irc.channel;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import de.rubenmaurer.punk.core.irc.messages.impl.Join;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Notification;
import de.rubenmaurer.punk.util.Template;

import java.util.HashMap;
import java.util.Map;

import static de.rubenmaurer.punk.core.Guardian.reporter;

/**
 * Actor for handling a single channel.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.1
 */
public class ChannelHandler extends AbstractActor {

    /**
     * The channel name
     */
    private String name;

    /**
     * The channel topic.
     */
    private String topic = Template.get("defaultChannelTopic").toString();

    /**
     * The list of connected clients.
     */
    private HashMap<String, ActorRef> connections = new HashMap<>();

    /**
     * Get the props for a new actor.
     *
     * @param name the channel name
     * @return the props
     */
    public static Props props(String name) {
        return Props.create(ChannelHandler.class, name);
    }

    /**
     * Constructor for a channelHandler.
     *
     * @param name the name of the channel
     */
    public ChannelHandler(String name) {
        this.name = name;
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * User enters a channel.
     *
     * @param joinMessage the join message
     */
    private void join(Join joinMessage) {
        ActorRef newOne = sender();
        StringBuilder builder = new StringBuilder();
        connections.put(joinMessage.getNickname(), newOne);

        for (Map.Entry<String, ActorRef> handler : connections.entrySet()) {
            String tpl = Notification.get(Notification.Reply.RPL_JOIN,
                    new String[]{ joinMessage.getNickname(), joinMessage.getHostname(), joinMessage.getChannel() });
            handler.getValue().tell(tpl, self());

            builder.append(String.format(" %s", handler.getKey()));
        }

        newOne.tell(Notification.get(Notification.Reply.RPL_TOPIC,
                new String[]{ joinMessage.getNickname(), name, topic }), self());

        newOne.tell(Notification.get(Notification.Reply.RPL_NAMREPLY,
                new String[]{ joinMessage.getNickname(), name, builder.toString() }), self());

        newOne.tell(Notification.get(Notification.Reply.RPL_ENDOFNAMES,
                new String[]{ joinMessage.getNickname(), name }), self());
    }

    /**
     * Process chat messsage.
     *
     * @param chat the chat message
     */
    private void chat(String chat) {
        for (Map.Entry<String, ActorRef> entry : connections.entrySet()) {
            if (entry.getValue() != sender())
                entry.getValue().tell(chat, self());
        }
    }

    /**
     * Receive and process messages.
     *
     * @return a receive object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Join.class, this::join)
                .match(String.class, this::chat)
                .build();
    }
}
