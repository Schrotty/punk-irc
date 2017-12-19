package de.rubenmaurer.punk.core.irc.messages.impl;

import de.rubenmaurer.punk.core.irc.messages.Message;

/**
 * ParserMessage type for parsing.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class ParseMessage extends Message {

    /**
     * The message to parse.
     */
    private String message;

    /**
     * Get the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Create a new message object.
     *
     * @param message the message
     */
    public ParseMessage(String message) {
        this.message = message;
    }
}
