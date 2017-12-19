package de.rubenmaurer.punk.core.irc.messages;

public class Logout extends Message {
    private final String message;

    public String getMessage() {
        return message;
    }

    Logout(String message) {
        this.message = message;
    }
}
