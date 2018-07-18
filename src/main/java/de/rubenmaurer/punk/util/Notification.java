package de.rubenmaurer.punk.util;

/**
 * Class for creating notification strings.
 * TODO: Methods per Reply/ Error
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Notification {

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
        RPL_WHOISUSER,
        RPL_NOTICE,
        RPL_JOIN,
        RPL_TOPIC,
        RPL_NAMREPLY,
        RPL_ENDOFNAMES
    }

    private Notification() {
    }

    public static String errUnknownCommand(String command) {
        return Template.get("ERR_UNKNOWNCOMMAND").multiple(
            new String[] {"host", "command"},
            new String[] {Settings.hostname(), command }
        );
    }

    public static String errNicknameInUse(String nickname) {
        return Template.get("ERR_NICKNAMEINUSE").multiple(
            new String[] { "nick", "host" },
            new String[] { nickname, Settings.hostname() }
        );
    }

    public static String errAlreadyRegistered(String hostname) {
        return Template.get("ERR_ALREADYREGISTRED").single("host", hostname);
    }

    public static String errNoSuchNick(String hostname, String nickname) {
        return Template.get("ERR_NOSUCHNICK").multiple(
            new String[]{ "host", "nick" },
            new String[]{ hostname, nickname }
        );
    }

    public static String errError(String hostname, String message) {
        return Template.get("ERROR").multiple(
                new String[] { "host", "message" },
                new String[] { hostname, message });
    }

    public static String errNoMessageOfTheDay() {
        return Template.get("ERR_NOMOTD").single("host", Settings.hostname());
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
                    new String[] { "host", "servername", "version", "nick" },
                    new String[] { Settings.hostname(), Settings.servername(), Settings.version(), values[0] });

        if (type.equals(Reply.RPL_CREATED))
            return Template.get("RPL_CREATED").multiple(
                    new String[] { "host", "date", "nick" },
                    new String[] { Settings.hostname(), Settings.buildDate(), values[0] });

        if (type.equals(Reply.RPL_MYINFO))
            return Template.get("RPL_MYINFO").multiple(
                    new String[] { "host", "servername", "version", "nick" },
                    new String[] { Settings.hostname(), Settings.servername(), Settings.version(), values[0] });

        if (type.equals(Reply.RPL_PRIVMSG))
            return Template.get("RPL_PRIVMSG").multiple(
                    new String[]{ "sender", "host", "target", "message" },
                    new String[]{ values[0], values[3], values[1], values[2] });

        if (type.equals(Reply.RPL_WHOISUSER))
            return Template.get("RPL_WHOISUSER").multiple(
                    new String[] { "nick", "user", "host", "real" },
                    new String[] { values[0], values[1], Settings.hostname(), values[3] });

        if (type.equals(Reply.RPL_NOTICE))
            return Template.get("RPL_NOTICE").multiple(
                    new String[]{ "sender", "host", "target", "message" },
                    new String[]{ values[0], values[3], values[1], values[2] });

        if (type.equals(Reply.RPL_JOIN))
            return Template.get("RPL_JOIN").multiple(
                    new String[]{ "nickname", "host", "channel" },
                    new String[]{ values[0], values[1], values[2] });

        if (type.equals(Reply.RPL_TOPIC))
            return Template.get("RPL_TOPIC").multiple(
                    new String[]{ "server", "nick", "channel", "topic" },
                    new String[]{ Settings.hostname(), values[0], values[1], values[2] });

        if (type.equals(Reply.RPL_NAMREPLY))
            return Template.get("RPL_NAMREPLY").multiple(
                    new String[]{ "server", "nick", "channel", "users" },
                    new String[]{ Settings.hostname(), values[0], values[1], values[2] });

        if (type.equals(Reply.RPL_ENDOFNAMES))
            return Template.get("RPL_ENDOFNAMES").multiple(
                    new String[]{ "server", "nick", "channel" },
                    new String[]{ Settings.hostname(), values[0], values[1] });

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
