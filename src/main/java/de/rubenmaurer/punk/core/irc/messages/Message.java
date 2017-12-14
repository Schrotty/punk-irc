package de.rubenmaurer.punk.core.irc.messages;

public abstract class Message {
    boolean request = true;
    boolean error = false;
    String errorMessage;

    public boolean isRequest() {
        return request;
    }

    public boolean hasError() {
        return error;
    }

    public String getError() {
        return errorMessage;
    }
}
