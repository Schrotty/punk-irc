package de.rubenmaurer.punk.core.irc.messages.impl;

import de.rubenmaurer.punk.core.irc.messages.Message;

public class Chat extends Message {
    private String target;
    private TargetType targetType;
    private Type type;
    private String message;

    public String getTarget() {
        return target;
    }

    public String getMessage() {
        return message;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public Type getType() {
        return type;
    }

    public Chat(String target, TargetType targetType, String message, Type type) {
        this.target = target;
        this.targetType = targetType;
        this.message = message;
        this.type = type;
    }

    public enum TargetType {
        CHANNEL,
        USER
    }

    public enum Type {
        PRIVMSG,
        NOTICE
    }
}
