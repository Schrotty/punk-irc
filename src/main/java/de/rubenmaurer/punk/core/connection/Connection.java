package de.rubenmaurer.punk.core.connection;

import akka.actor.ActorRef;
import de.rubenmaurer.punk.util.Template;

/**
 * Connection class for storing all needed information about a connection.
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
        this.nickname = nickname;
        tryLogin();
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
        this.realname = realname;
        tryLogin();
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
     */
    private Connection(ActorRef connection) {
        this.connection = connection;
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
     * Try to login the connection.
     */
    private void tryLogin() {
        if (!isLogged() && (!nickname.isEmpty() && !realname.isEmpty())) {
            connection.tell(Template.get("RPL_WELCOME").multiple(
                    new String[]{"nick", "user", "host"},
                    new String[]{nickname, realname, "localhost"}
            ), ActorRef.noSender());

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
     * Create a new connection object.
     *
     * @param connection the used connection actor
     * @return the created connection object
     */
    public static Connection create(ActorRef connection) {
        return new Connection(connection);
    }
}
