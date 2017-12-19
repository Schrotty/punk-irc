package de.rubenmaurer.punk.core.irc.messages;

/**
 * A IRC Join ChatMessage.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class Join extends Message {

    /**
     * The used nickname.
     */
    private final String nickname;

    /**
     * The channel to join.
     */
    private final String channel;

    /**
     * The own hostname.
     */
    private final String hostname;


    /**
     * Get the nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Get the channel.
     *
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Get the hostname.
     *
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    Join(String channel) {
        this.channel = channel;
    }

    /**
     * Create new join message
     *
     * @param nickname the used nickname
     * @param channel the channel to join
     * @param hostname the hostname
     */
    Join(String nickname, String channel, String hostname) {
        this.nickname = nickname;
        this.channel = channel;
        this.hostname = hostname;
    }
}
