package de.rubenmaurer.punk.core.irc.channel;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import de.rubenmaurer.punk.core.irc.messages.Chat;
import de.rubenmaurer.punk.core.irc.messages.Join;
import de.rubenmaurer.punk.util.Notification;
import de.rubenmaurer.punk.util.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * Actor for handling a single channel.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class ChannelHandler extends AbstractActor {

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
     * @return the props
     */
    public static Props props() {
        return Props.create(ChannelHandler.class);
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
                new String[]{ joinMessage.getNickname(), self().path().name(), topic }), self());

        newOne.tell(Notification.get(Notification.Reply.RPL_NAMREPLY,
                new String[]{ joinMessage.getNickname(), self().path().name(), builder.toString() }), self());

        newOne.tell(Notification.get(Notification.Reply.RPL_ENDOFNAMES,
                new String[]{ joinMessage.getNickname(), self().path().name() }), self());
    }

    /**
     * Process chat messsage.
     *
     * @param chat the chat message
     */
    private void chat(Chat chat) {
        for (Map.Entry<String, ActorRef> entry : connections.entrySet()) {
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
                .match(Chat.class, this::chat)
                .build();
    }
}
