package de.rubenmaurer.punk.core.irc.client;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.io.Tcp;
import akka.io.TcpMessage;
import akka.util.ByteString;
import de.rubenmaurer.punk.core.irc.PunkServer;
import de.rubenmaurer.punk.core.irc.messages.*;
import de.rubenmaurer.punk.core.reporter.Report;
import de.rubenmaurer.punk.util.Notification;
import de.rubenmaurer.punk.util.Settings;
import de.rubenmaurer.punk.util.Template;

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
     * The used connection object for storing data.
     */
    private Connection connection;
    private ConnectionHandler handler;

    /**
     * Contructor for a new ConnectionHandler
     *
     * @param hostname the hostname
     */
    public ConnectionHandler(String hostname) {
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
        return online; //TODO: Replace
    }

    /**
     * Set connection nickname.
     *
     * @param nickname the new nickname
     */
    public void setNickname(String nickname) {
        if (!ConnectionManager.hasNickname(nickname)) {
            if (isOnline()) {
                remote.tell(Notification.get(Notification.Reply.RPL_NICKCHANGE,
                        new String[] { this.nickname, nickname }), self());
            }

            this.nickname = nickname;
            tryLogin();
            return;
        }

        remote.tell(Notification.get(Notification.Error.ERR_NICKNAMEINUSE, nickname), self());
    }

    /**
     * Set connection real-name
     *
     * @param realname the new real-name
     */
    public void setRealname(String realname) {
        if(this.realname.isEmpty()) {
            this.realname = realname;
            tryLogin();
            return;
        }

        remote.tell(Notification.get(Notification.Error.ERR_ALREADYREGISTRED, Settings.hostname()), self());
    }

    /**
     * Try to login the connection.
     */
    private void tryLogin() {
        if (!isOnline() && (!nickname.isEmpty() && !realname.isEmpty())) {
            remote.tell(Notification.get(Notification.Reply.RPL_WELCOME,
                    new String[] { nickname, realname, hostname }), self());

            remote.tell(Notification.get(Notification.Reply.RPL_YOURHOST), self());
            remote.tell(Notification.get(Notification.Reply.RPL_CREATED), self());
            remote.tell(Notification.get(Notification.Reply.RPL_MYINFO), self());

            online = true;
        }
    }

    private void applyChange(Change change) {
        if (change.field.equals(Change.Field.NICKNAME)) {
            nickname = change.getValue();
            return;
        }

        if (change.field.equals(Change.Field.REALNAME)) {
            realname = change.getValue();
        }
    }

    /**
     * Logout a connection.
     *
     * @param message the quit message
     */
    public void logout(String message) {
        ConnectionManager.connections.remove(this);

        remote.tell(Notification.get(Notification.Error.ERROR, new String[]{ message, hostname }), self());
        getContext().stop(getSelf());
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
                .match(Tcp.Received.class, msg -> {
                    if (connection == null && remote == null) {
                        remote = getSender();
                        connection = Connection.create(getSelf(), hostname);
                        ConnectionManager.connections.add(connection);
                    }

                    //System.out.println(msg.data().decodeString("US-ASCII"));
                    PunkServer.getParser().tell(ParseMessage.create(msg.data().decodeString("US-ASCII"), connection), self());
                })
                .match(Tcp.ConnectionClosed.class, msg -> getContext().stop(getSelf()))
                .match(String.class, s -> remote.tell(TcpMessage.write(ByteString.fromString(s.intern() + '\r' + '\n'), TcpMessage.noAck()), getSelf()))
                .match(Connection.class, con -> {
                    int index = ConnectionManager.connections.indexOf(connection);
                    if (index != -1) ConnectionManager.connections.set(index, con);

                    connection = con;
                })
                .match(ChatMessage.class, s -> s.getTarget().tell(s.toString(), self()))
                .match(WhoIs.class, msg -> {
                    if (msg.isRequest()) context().parent().tell(msg, self());
                    if (!msg.isRequest()) connection.finishRequest(msg);
                })
                .match(Join.class, j -> PunkServer.getChannelManager().tell(MessageBuilder.join(nickname, j.getChannel(), hostname), self()))
                .match(Chat.class, c -> {
                    if (!c.hasError()) connection.relay(c);
                    if (c.hasError()) remote.tell(c.getError(), self());
                })
                .match(Info.PING.getClass(), s -> remote.tell(Template.get("pong").toString(), self()))
                .match(Info.MOTD.getClass(), s -> remote.tell(Settings.messageofTheDay(), self()))
                .match(Change.class, this::applyChange)
                .build();
    }
}