package de.rubenmaurer.punk.core.irc.messages;

/**
 * Abstract class for actor messages.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.1
 */
public abstract class Message {

    /**
     * Is this a request?
     */
    protected boolean request = true;

    /**
     * Is this an error?
     */
    protected boolean error = false;

    /**
     * The error message.
     */
    protected String errorMessage;

    /**
     * Has error?
     *
     * @return has error?
     */
    public boolean hasError() {
        return error;
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
