package de.rubenmaurer.punk.core.connection;

/**
 * Message type for parsing.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Message {

    /**
     * The message to parse.
     */
    private String message;

    /**
     * The connection for parsing.
     */
    private Connection connection;

    /**
     * Get the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the connection.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Create a new message object.
     *
     * @param message the message
     * @param connection the connection
     */
    private Message(String message, Connection connection) {
        this.connection = connection;
        this.message = message;
    }

    /**
     * Create a new message.
     *
     * @param message the message
     * @param connection the connection
     * @return the created message
     */
    public static Message create(String message, Connection connection) {
        return new Message(message, connection);
    }
}
