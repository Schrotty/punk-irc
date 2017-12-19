package de.rubenmaurer.punk.core.irc.messages.impl;

import de.rubenmaurer.punk.core.irc.messages.Message;

public class Logout extends Message {
    private final String message;

    public String getMessage() {
        return message;
    }

    public Logout(String message) {
        this.message = message;
    }
}
