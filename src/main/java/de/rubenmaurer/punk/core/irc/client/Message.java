package de.rubenmaurer.punk.core.irc.client;

import akka.actor.ActorRef;
import de.rubenmaurer.punk.util.Notification;

public class Message {
    private final ActorRef target;

    public ActorRef getTarget() {
        return target;
    }

    private final String nickname;

    public String getNickname() {
        return nickname;
    }

    private final String sender;

    public String getSender() {
        return sender;
    }

    private final String message;

    public String getMessage() {
        return message;
    }

    private Message(ActorRef target, String nickname, String message, String sender) {
        this.target = target;
        this.message = message;
        this.nickname = nickname;
        this.sender = sender;
    }

    public static Message create(ActorRef target, String nickname, String message, String sender) {
        return new Message(target, nickname, message, sender);
    }

    @Override
    public String toString() {
        return Notification.get(Notification.Reply.RPL_PRIVMSG,
                new String[] { sender, nickname, message });
    }
}
