package de.rubenmaurer.punk.core.irc.messages;

/**
 * Abstract class for actor messages.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public abstract class Message {

    /**
     * Is this a request?
     */
    boolean request = true;

    /**
     * Is this an error?
     */
    boolean error = false;

    /**
     * Is this message empty?
     */
    boolean empty = true;

    /**
     * The error message.
     */
    String errorMessage;

    /**
     * Is a request?
     *
     * @return is a request?
     */
    public boolean isRequest() {
        return request;
    }

    /**
     * Has error?
     *
     * @return has error?
     */
    public boolean hasError() {
        return error;
    }

    /**
     * Is this message empty?
     *
     * @return is emty?
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Get the error message.
     *
     * @return the error message.
     */
    public String getError() {
        return errorMessage;
    }
}
