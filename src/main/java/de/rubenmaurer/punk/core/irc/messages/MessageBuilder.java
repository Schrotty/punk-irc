package de.rubenmaurer.punk.core.irc.messages;

public class MessageBuilder {
    public static Message whoIs(String nickname) {
        return new WhoIs(nickname);
    }

    public static Message whoIs(String nickname, String realname) {
        return new WhoIs(nickname, realname);
    }

    public static Message whoIs(String nickname, String realname, String message) {
        return new WhoIs(nickname, realname, message);
    }
}
