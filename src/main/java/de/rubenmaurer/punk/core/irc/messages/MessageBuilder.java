package de.rubenmaurer.punk.core.irc.messages;

import de.rubenmaurer.punk.core.irc.messages.impl.*;

/**
 * Builder for all types of messages.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.1
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
     * Create a new join message.
     *
     * @param channel the channel to join
     * @return the message
     */
    public static Message join(String channel) {
        return new Join(channel);
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
     * Create a chat message.
     *
     * @param target the target
     * @param targetType the message targetType
     * @param message the message
     * @return the message
     */
    public static Message chat(String target, Chat.TargetType targetType, String message, Chat.Type type) {
        return new Chat(target, targetType, message, type);
    }

    /**
     * Create a logout message.
     *
     * @param message the quit message
     * @return the message
     */
    public static Message logout(String message) {
        return new Logout(message);
    }

    /**
     * Create a new change message.
     *
     * @param field the field to change
     * @param value the new field value
     * @return the message
     */
    public static Message change(Change.Field field, String value) {
        return new Change(field, value);
    }

    /**
     * Create a new parse message.
     *
     * @param message the message to parse
     * @return the message
     */
    public static Message parse(String message) {
        return new ParseMessage(message);
    }
}
