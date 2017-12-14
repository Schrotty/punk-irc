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
        ERR_NICKNAMEINUSE,
        ERR_ALREADYREGISTRED,
        ERR_NOSUCHNICK,
        ERROR,
        ERR_NOMOTD
    }

    /**
     * Reply types
     */
    public enum Reply {
        RPL_WELCOME,
        RPL_YOURHOST,
        RPL_CREATED,
        RPL_MYINFO,
        RPL_PRIVMSG,
        RPL_NICKCHANGE,
        RPL_WHOISUSER
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

        if (type.equals(Error.ERR_ALREADYREGISTRED))
            return Template.get("ERR_ALREADYREGISTRED").single("host", values[0]);

        if (type.equals(Error.ERROR))
            return Template.get("ERROR").multiple(
                    new String[] { "host", "message" },
                    new String[] { Settings.hostname(), values[0] });

        if (type.equals(Error.ERR_NOSUCHNICK))
            return Template.get("ERR_NOSUCHNICK").multiple(
                    new String[]{ "host", "nick" },
                    new String[]{ Settings.hostname(), values[0] });

        if (type.equals(Error.ERR_NOMOTD))
            return Template.get("ERR_NOMOTD").single("host", Settings.hostname());

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
     * Get a reply string filled with a single value.
     *
     * @param type reply type
     * @return the reply string
     */
    public static String get(Error type) {
        return get(type, new String[] { "" });
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

        if (type.equals(Reply.RPL_NICKCHANGE))
            return Template.get("RPL_NICKCHANGE").multiple(
                    new String[] { "old", "host", "new" },
                    new String[] { values[0], Settings.hostname(), values[1] });

        if (type.equals(Reply.RPL_YOURHOST))
            return Template.get("RPL_YOURHOST").multiple(
                    new String[] { "host", "servername", "version" },
                    new String[] { Settings.hostname(), Settings.servername(), Settings.version() });

        if (type.equals(Reply.RPL_CREATED))
            return Template.get("RPL_CREATED").multiple(
                    new String[] { "host", "date" },
                    new String[] { Settings.hostname(), Settings.buildDate() });

        if (type.equals(Reply.RPL_MYINFO))
            return Template.get("RPL_MYINFO").multiple(
                    new String[] { "host", "servername", "version" },
                    new String[] { Settings.hostname(), Settings.servername(), Settings.version() });

        if (type.equals(Reply.RPL_PRIVMSG))
            return Template.get("RPL_PRIVMSG").multiple(
                    new String[]{ "sender", "host", "target", "message" },
                    new String[]{ values[0], Settings.hostname(), values[1], values[2] });

        if (type.equals(Reply.RPL_WHOISUSER))
            return Template.get("RPL_WHOISUSER").multiple(
                    new String[] { "nick", "user", "host", "real" },
                    new String[] { values[0], values[1], Settings.hostname(), values[3] }
            );

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

    /**
     * Get a reply string filled with a single value.
     *
     * @param type reply type
     * @return the reply string
     */
    public static String get(Reply type) {
        return get(type, new String[] { "" });
    }
}
