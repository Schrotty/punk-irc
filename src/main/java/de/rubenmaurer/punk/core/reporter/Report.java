package de.rubenmaurer.punk.core.reporter;

/**
 * Class for reporting.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Report {

    /**
     * Report types.
     */
    public enum Type {
        INFO,
        ERROR,
        ONLINE,
        NONE,
        OFFLINE
    }

    /**
     * The Report type.
     */
    private Type type;

    /**
     * Name of the sender.
     */
    private String message;

    /**
     * Gets report type.
     *
     * @return the message
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets report message.
     *
     * @return the sender
     */
    public String getMessage() {
        return message;
    }

    /**
     * Create new message.
     *
     * @param type the msg
     * @param message the sender name
     * @return the message
     */
    public static Report create(Type type, String message) {
        return new Report(type, message);
    }

    /**
     * Create new message.
     *
     * @param type the msg
     * @return the message
     */
    public static Report create(Type type) {
        return new Report(type);
    }

    /**
     * Instantiates a new Report.
     *
     * @param type the msg
     */
    private Report(Type type) {
        this.type = type;
    }

    /**
     * Instantiates a new Report.
     *
     * @param type the msg
     * @param message the sender name
     */
    private Report(Type type, String message) {
        this(type);
        this.message = message;
    }
}
