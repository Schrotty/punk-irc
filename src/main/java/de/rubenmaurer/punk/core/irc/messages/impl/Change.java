package de.rubenmaurer.punk.core.irc.messages.impl;

import de.rubenmaurer.punk.core.irc.messages.Message;

public class Change extends Message {
    public enum Field {
        NICKNAME,
        REALNAME
    }

    private final Field field;
    private final String value;

    public Field getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public Change(Field field, String value) {
        this.field = field;
        this.value = value;
    }
}
