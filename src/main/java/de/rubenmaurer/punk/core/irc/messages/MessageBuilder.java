package de.rubenmaurer.punk.core.irc.messages;

import akka.actor.ActorRef;

/**
 * Builder for all types of messages.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class MessageBuilder {

    /**
     * Create a whoIs message only with a nickname.
     *
     * @param nickname the nickname
     * @return the message
     */
    public static Message whoIs(String nickname) {
        return new WhoIs(nickname);
    }

    /**
     * Create a whoIs message with a nickname, and a realname.
     *
     * @param nickname the nickname
     * @param realname the realname
     * @return the message
     */
    public static Message whoIs(String nickname, String realname) {
        return new WhoIs(nickname, realname);
    }

    /**
     * Create a whoIs message with a nickname, a realname and an error message.
     *
     * @param nickname the nickname
     * @param realname the realname
     * @param message the error message
     * @return the message
     */
    public static Message whoIs(String nickname, String realname, String message) {
        return new WhoIs(nickname, realname, message);
    }

    /**
     * Create a join message with a nickname, the channel to join and the hostname.
     *
     * @param nickname the nickname
     * @param channel the channel to join
     * @param hostname the hostname
     * @return the message
     */
    public static Message join(String nickname, String channel, String hostname) {
        return new Join(nickname, channel, hostname);
    }

    /**
     * Create a chatMessage.
     *
     * @param target the target
     * @param nickname the nickname to use
     * @param message the message
     * @param sender the sender
     * @param type message type
     * @param hostname the hostname
     * @return the message
     */
    public static Message chatMessage(ActorRef target, String nickname, String message, String sender, ChatMessage.Type type, String hostname) {
        return new ChatMessage(target, nickname, message, sender, type, hostname);
    }

    /**
     * Create a chat message.
     *
     * @param target the target
     * @param type the message type
     * @param message the message
     * @return the message
     */
    public static Message chat(String target, Chat.Type type, String message) {
        return new Chat(target, type, message);
    }
}
