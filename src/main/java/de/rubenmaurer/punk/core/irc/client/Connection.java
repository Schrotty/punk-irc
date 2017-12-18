package de.rubenmaurer.punk.core.irc.client;

import akka.actor.ActorRef;
import akka.io.TcpMessage;
import de.rubenmaurer.punk.core.irc.PunkServer;
import de.rubenmaurer.punk.core.irc.messages.Chat;
import de.rubenmaurer.punk.core.irc.messages.ChatMessage;
import de.rubenmaurer.punk.core.irc.messages.MessageBuilder;
import de.rubenmaurer.punk.core.irc.messages.WhoIs;
import de.rubenmaurer.punk.util.Notification;
import de.rubenmaurer.punk.util.Settings;
import de.rubenmaurer.punk.util.Template;

/**
 * Connection class for storing all needed information about a connection.
 * Will replaced with a messaging system so everything can be stored in the ConnectionHandler.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Connection {

    /**
     * The used connection actor.
     */
    private final ActorRef connection;

    /**
     * The nickname of this connection.
     */
    private String nickname = "";

    /**
     * The real-name of this connection.
     */
    private String realname = "";

    /**
     * The hostname of this connection.
     */
    private String hostname;

    /**
     * Is connection logged in?
     */
    private boolean login = false;

    /**
     * Get connection nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Set connection nickname.
     *
     * @param nickname the new nickname
     */
    public void setNickname(String nickname) {
        if (!ConnectionManager.hasNickname(nickname)) {
            if (login) {
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
     * Get connection real-name.
     *
     * @return the real-name
     */
    public String getRealname() {
        return realname;
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

        write(Notification.get(Notification.Error.ERR_ALREADYREGISTRED, Settings.hostname()));
    }

    /**
     * Get the connections hostname.
     *
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Get the connection
     *
     * @return the connection
     */
    public ActorRef getConnection() {
        return connection;
    }

    /**
     * Create new connection.
     *
     * @param connection the used connection actor
     * @param hostname the hostname
     */
    private Connection(ActorRef connection, String hostname) {
        this.connection = connection;
        this.hostname = hostname;
    }

    /**
     * Write a string on the connection.
     *
     * @param message the message to write
     */
    public void write(String message) {
        connection.tell(message, ActorRef.noSender());
    }

    /**
     * Write a string to a specific connection.

     * @param message the message to write
     */
    private void write(Object message) {
        connection.tell(message, ActorRef.noSender());
    }

    /**
     * Send message to another user.
     *
     * @param target the user to Chat with
     * @param message the message to send
     */
    public void chat(String target, String message) {
        if (!target.startsWith("#")) {
            for (Connection con : ConnectionManager.connections) {
                if (con.nickname.equals(target)) {
                    write(ChatMessage.create(con.getConnection(), target, message, this.nickname, ChatMessage.Type.PRIVMSG, hostname));
                    return;
                }
            }

            write(Notification.get(Notification.Error.ERR_NOSUCHNICK, new String[]{target, hostname}));
            return;
        }

        PunkServer.getChannelManager().tell(MessageBuilder.chat(target, Chat.Type.CHANNEL, message), connection);
    }

    /**
     * Send message to another user.
     *
     * @param nickname the user to Chat with
     * @param message the message to send
     */
    public void notice(String nickname, String message) {
        for (Connection con : ConnectionManager.connections) {
            if (con.nickname.equals(nickname)) {
                write(ChatMessage.create(con.getConnection(), nickname, message, this.nickname, ChatMessage.Type.NOTICE, hostname));
            }
        }
    }

    /**
     * Logout a connection.
     *
     * @param message the quit message
     */
    public void logout(String message) {
        ConnectionManager.connections.remove(this);

        write(Notification.get(Notification.Error.ERROR, new String[]{ message, hostname }));
        connection.tell(TcpMessage.close(), ActorRef.noSender());
    }

    /**
     * Try to login the connection.
     */
    private void tryLogin() {
        if (!isLogged() && (!nickname.isEmpty() && !realname.isEmpty())) {
            connection.tell(Notification.get(Notification.Reply.RPL_WELCOME,
                    new String[] { nickname, realname, hostname }), ActorRef.noSender());

            write(Notification.get(Notification.Reply.RPL_YOURHOST));
            write(Notification.get(Notification.Reply.RPL_CREATED));
            write(Notification.get(Notification.Reply.RPL_MYINFO));

            login = true;
        }
    }

    /**
     * Is connection logged in
     *
     * @return is logged in?
     */
    private boolean isLogged() {
        return login;
    }

    /**
     * Plays ping pong...
     */
    public void pong() {
        write(Template.get("pong").toString());
    }

    /**
     * Who is the given user?
     *
     * @param user the given user
     */
    private void whoIs(WhoIs user) {
        write(Notification.get(Notification.Reply.RPL_WHOISUSER,
                new String[] { user.getNickname(), user.getNickname(), Settings.hostname(), user.getRealname() }));
    }

    /**
     * Join a channel.
     *
     * @param channel the name of the channel to join
     */
    public void join(String channel) {
        connection.tell(MessageBuilder.join(nickname, channel, hostname), ActorRef.noSender());
    }

    /**
     * Perform action based on request to finish.
     *
     * @param message the request to process
     */
    public void finishRequest(de.rubenmaurer.punk.core.irc.messages.Message message) {
        if (!message.hasError()) {
            if (message.getClass() == WhoIs.class) whoIs((WhoIs) message);
            return;
        }

        write(message.getError());
    }

    /**
     * Create a new connection object.
     *
     * @param connection the used connection actor
     * @param hostname the hostname
     * @return the created connection object
     */
    public static Connection create(ActorRef connection, String hostname) {
        return new Connection(connection, hostname);
    }

    public void relay(Chat chat) {
        String val = Notification.get(Notification.Reply.RPL_PRIVMSG,
            new String[]{ nickname, chat.getTarget(), chat.getMessage(), hostname });

        write(val);
    }
}
