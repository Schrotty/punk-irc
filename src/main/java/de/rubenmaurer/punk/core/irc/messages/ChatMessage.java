package de.rubenmaurer.punk.core.irc.messages;

import akka.actor.ActorRef;
import de.rubenmaurer.punk.util.Notification;

/**
 * IRC chat message class.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class ChatMessage {

    /**
     * Type of the chat message.
     */
    private Type type;

    /**
     * The target actor.
     */
    private final ActorRef target;

    /**
     * Get the target actor.
     *
     * @return the target actor
     */
    public ActorRef getTarget() {
        return target;
    }

    /**
     * The targets nickname.
     */
    private final String nickname;

    /**
     * Get the targets nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * The senders nickname.
     */
    private final String sender;

    /**
     * Get the senders nickname.
     *
     * @return the nickname
     */
    public String getSender() {
        return sender;
    }

    /**
     * The message to send.
     */
    private final String message;

    /**
     * Get the message to send.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * The hostname
     */
    private String hostname;

    /**
     * Get the hostname
     *
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Constructor for a new message object.
     *
     * @param target the target actor
     * @param nickname the targets nickname
     * @param message the message
     * @param sender the senders nickname
     */
    private ChatMessage(ActorRef target, String nickname, String message, String sender, Type type, String hostname) {
        this.target = target;
        this.message = message;
        this.nickname = nickname;
        this.sender = sender;
        this.type = type;
        this.hostname = hostname;
    }

    /**
     * Create a new message object.
     *
     * @param target the target actor
     * @param nickname the targets nickname
     * @param message the message
     * @param sender the senders nickname
     * @return the created message
     */
    public static ChatMessage create(ActorRef target, String nickname, String message, String sender, Type type, String hostname) {
        return new ChatMessage(target, nickname, message, sender, type, hostname);
    }

    /**
     * Get IRC command string of this message
     *
     * @return the command string
     */
    @Override
    public String toString() {
        if (type.equals(Type.PRIVMSG)) {
            return Notification.get(Notification.Reply.RPL_PRIVMSG,
                    new String[]{sender, nickname, message, hostname});
        }

        return Notification.get(Notification.Reply.RPL_NOTICE,
                new String[]{sender, nickname, message, hostname});
    }

    public enum Type {
        PRIVMSG,
        NOTICE
    }
}
