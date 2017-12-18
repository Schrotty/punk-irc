package de.rubenmaurer.punk.core.irc.messages;

public class Chat extends Message {
    private String target;
    private Type type;
    private String message;

    public String getTarget() {
        return target;
    }

    public String getMessage() {
        return message;
    }

    Chat(String target, Type type, String message) {
        this.target = target;
        this.type = type;
        this.message = message;
    }

    public enum Type {
        CHANNEL,
        USER
    }
}
