package de.rubenmaurer.punk.util;

/**
 * Class for creating notification strings.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Notification {

    /**
     * Error types
     */
    public enum Error {
        ERR_UNKNOWNCOMMAND,
        ERR_NICKNAMEINUSE
    }

    /**
     * Reply types
     */
    public enum Reply {
        RPL_WELCOME
    }

    private Notification() {
    }

    /**
     * Get a reply string filled with multiple values.
     *
     * @param type the reply type
     * @param values the values
     * @return the reply string
     */
    public static String get(Error type, String[] values) {
        if (type.equals(Error.ERR_UNKNOWNCOMMAND))
            return Template.get("ERR_UNKNOWNCOMMAND").multiple(
                    new String[] {"client", "command"}, values);

        if (type.equals(Error.ERR_NICKNAMEINUSE))
            return Template.get("ERR_NICKNAMEINUSE").multiple(
                    new String[] { "nick", "host" },
                    new String[] { values[0], Settings.hostname() });

        return "";
    }

    /**
     * Get a reply string filled with a single value.
     *
     * @param type reply type
     * @param value the value
     * @return the reply string
     */
    public static String get(Error type, String value) {
        return get(type, new String[] { value });
    }

    /**
     * Get a reply string filled with multiple values.
     *
     * @param type the reply type
     * @param values the values
     * @return the reply string
     */
    public static String get(Reply type, String[] values) {
        if (type.equals(Reply.RPL_WELCOME))
            return Template.get("RPL_WELCOME").multiple(
                    new String[]{ "nick", "user", "host"}, values);

        return "";
    }

    /**
     * Get a reply string filled with a single value.
     *
     * @param type reply type
     * @param value the value
     * @return the reply string
     */
    public static String get(Reply type, String value) {
        return get(type, new String[] { value });
    }
}
