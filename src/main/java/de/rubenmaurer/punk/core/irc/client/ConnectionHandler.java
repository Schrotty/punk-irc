package de.rubenmaurer.punk.core.irc.client;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import de.rubenmaurer.punk.core.irc.PunkServer;
import de.rubenmaurer.punk.core.irc.channel.ChannelManager;
import de.rubenmaurer.punk.core.irc.messages.*;
import de.rubenmaurer.punk.core.irc.messages.impl.*;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Notification;
import de.rubenmaurer.punk.util.Settings;
import de.rubenmaurer.punk.util.Template;

import java.util.HashMap;
import java.util.Map;

import static de.rubenmaurer.punk.core.Guardian.reporter;

/**
 * Actor for handling a established connection.
 *
 * @author Ruben Maurer
 * @version 1.2
 * @since 1.0
 */
public class ConnectionHandler extends AbstractActor {

    /**
     * Is actor auth?
     */
    private boolean online;

    /**
     * The remote endpoint actor.
     */
    private ActorRef remote;

    /**
     * The clients nickname.
     */
    private String nickname;

    /**
     * The clients realname.
     */
    private String realname;

    /**
     * The remote hostname.
     */
    private final String hostname;

    /**
     * Contructor for a new ConnectionHandler
     *
     * @param hostname the hostname
     */
    public ConnectionHandler(String hostname) {
        this.nickname = "";
        this.realname = "";
        this.hostname = hostname;
    }

    /**
     * Get needed props for creating a new actor.
     *
     * @return the props for creating
     */
    public static Props props(String hostname) {
        return Props.create(ConnectionHandler.class, hostname);
    }

    /**
     * Is this handler auth?
     *
     * @return is auth?
     */
    private boolean isOnline() {
        return online;
    }

    /**
     * Set connection nickname.
     *
     * @param nickname the new nickname
     */
    public void setNickname(String nickname) {
        if (!ConnectionManager.hasNickname(nickname)) {
            if (isOnline()) {
                write(Notification.get(Notification.Reply.RPL_NICKCHANGE,
                        new String[] { this.nickname, nickname }));
            }

            this.nickname = nickname;
            tryLogin();
            return;
        }

        write(Notification.get(Notification.Error.ERR_NICKNAMEINUSE, nickname));
    }

    /**
     * Set connection real-name
     *
     * @param realname the new real-name
     */
    private void setRealname(String realname) {
        if(this.realname.isEmpty()) {
            this.realname = realname;
            tryLogin();
            return;
        }

        write(Notification.get(Notification.Error.ERR_ALREADYREGISTRED, Settings.hostname()));
    }

    /**
     * Try to login the connection.
     */
    private void tryLogin() {
        if (!isOnline() && (!nickname.isEmpty() && !realname.isEmpty())) {
            write(Notification.get(Notification.Reply.RPL_WELCOME,
                    new String[] { nickname, realname, hostname }));

            write(Notification.get(Notification.Reply.RPL_YOURHOST));
            write(Notification.get(Notification.Reply.RPL_CREATED));
            write(Notification.get(Notification.Reply.RPL_MYINFO));

            ConnectionManager.connections.put(nickname, self());
            online = true;
        }
    }

    /**
     * Changes a attribute.
     *
     * @param change the change message
     */
    private void applyChange(Change change) {
        if (change.getField().equals(Change.Field.NICKNAME)) {
            setNickname(change.getValue());
            return;
        }

        if (change.getField().equals(Change.Field.REALNAME)) {
            setRealname(change.getValue());
        }
    }

    /**
     *
     *
     * @param chat
     */
    private void relay(Chat chat) {
        HashMap<String, ActorRef> targets =
                chat.getTargetType().equals(Chat.TargetType.USER) ? ConnectionManager.connections : ChannelManager.channels;

        for (Map.Entry<String, ActorRef> entry : targets.entrySet()) {
            if (entry.getKey().equals(chat.getTarget())) {
                entry.getValue().tell(Notification.get(Notification.Reply.RPL_PRIVMSG,
                        new String[]{ nickname, chat.getTarget(), chat.getMessage(), hostname }), self());
                return;
            }
        }

        if (chat.getType().equals(Chat.Type.PRIVMSG))
            write(Notification.get(Notification.Error.ERR_NOSUCHNICK, new String[]{chat.getTarget(), hostname}));
    }

    /**
     * Logout a connection.
     *
     * @param message the quit message
     */
    private void logout(Logout message) {
        ConnectionManager.connections.remove(nickname);

        write(Notification.get(Notification.Error.ERROR, new String[]{ message.getMessage(), hostname }));
        getContext().stop(getSelf());
    }

    /**
     * Write a string to a specific connection.

     * @param message the message to write
     */
    private void write(String message) {
        remote.tell(TcpMessage.write(ByteString.fromString(message.intern() + '\r' + '\n'), TcpMessage.noAck()), getSelf());
    }

    /**
     * Gets fired before startup.
     */
    @Override
    public void preStart() {
        reporter().tell(Report.create(Report.Type.ONLINE), self());
    }

    /**
     * Gets fired after stop.
     */
    @Override
    public void postStop() {
        reporter().tell(Report.create(Report.Type.OFFLINE), self());
    }

    /**
     * Handles incoming messages and process them.
     *
     * @return a receiveBuilder object
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Template.get("pong").toString(), s -> write("PONG"))
                .matchEquals(Template.get("motd").toString(), s -> write(Settings.messageofTheDay()))
                .matchEquals(42, s -> sender().tell(realname, self()))
                .match(Tcp.Received.class, msg -> {
                    if (remote == null) {
                        remote = getSender();
                    }

                    PunkServer.getParser().tell(MessageBuilder.parse(msg.data().decodeString("US-ASCII")), self());
                })
                .match(Tcp.ConnectionClosed.class, msg -> getContext().stop(getSelf()))
                .match(String.class, this::write)
                .match(WhoIs.class, msg -> {
                    //TODO: Complete rewrite needed!
                    /*if (msg.isRequest()) context().parent().tell(msg, self());
                    if (!msg.isRequest()) connection.finishRequest(msg);*/
                })
                .match(Join.class, j -> PunkServer.getChannelManager().tell(MessageBuilder.join(nickname, j.getChannel(), hostname), self()))
                .match(Chat.class, c -> {
                    if (!c.hasError()) relay(c);
                    if (c.hasError()) write(c.getError());
                })
                .match(Change.class, this::applyChange)
                .match(Logout.class, this::logout)
                .build();
    }
}