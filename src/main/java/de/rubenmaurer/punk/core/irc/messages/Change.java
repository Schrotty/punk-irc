package de.rubenmaurer.punk.core.irc.messages;

public class Change extends Message {
    public enum Field {
        NICKNAME,
        REALNAME
    }

    public final Field field;
    public final String value;

    public Field getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    private Field getField() {
        return field;
    }

    private String getValue() {
        return value;
    }

    Change(Field field, String value) {
        this.field = field;
        this.value = value;
    }
}
