package de.rubenmaurer.punk.core.irc.messages.impl;

import de.rubenmaurer.punk.core.irc.messages.Message;

/**
 * A whoIs message for the IRC command 'WHOIS'.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class WhoIs extends Message {

    /**
     * The nickname
     */
    private String nickname;

    /**
     * The realname
     */
    private String realname;

    /**
     * Get the nickname.
     *
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Get the realname
     *
     * @return the realname
     */
    public String getRealname() {
        return realname;
    }

    /**
     * Constructor for a new whoIs message.
     *
     * @param nickname the nickname
     */
    public WhoIs(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Constructor for a new whoIs message.
     *
     * @param nickname the nickname
     * @param realname the realname
     */
    public WhoIs(String nickname, String realname) {
        this.nickname = nickname;
        this.realname = realname;
        request = false;
    }

    /**
     * Constructor for a new whoIs message.
     *
     * @param nickname the nickname
     * @param realname the realname
     * @param message the message
     */
    public WhoIs(String nickname, String realname, String message) {
        this(nickname, realname);
        this.errorMessage = message;
        this.error = true;
    }
}
