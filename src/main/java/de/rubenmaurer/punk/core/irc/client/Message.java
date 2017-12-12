package de.rubenmaurer.punk.core.irc.client;

import akka.actor.ActorRef;
import de.rubenmaurer.punk.util.Notification;

/**
 * IRC chat message class.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Message {

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
     * Constructor for a new message object.
     *
     * @param target the target actor
     * @param nickname the targets nickname
     * @param message the message
     * @param sender the senders nickname
     */
    private Message(ActorRef target, String nickname, String message, String sender) {
        this.target = target;
        this.message = message;
        this.nickname = nickname;
        this.sender = sender;
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
    public static Message create(ActorRef target, String nickname, String message, String sender) {
        return new Message(target, nickname, message, sender);
    }

    /**
     * Get IRC command string of this message
     *
     * @return the command string
     */
    @Override
    public String toString() {
        return Notification.get(Notification.Reply.RPL_PRIVMSG,
                new String[] { sender, nickname, message });
    }
}
