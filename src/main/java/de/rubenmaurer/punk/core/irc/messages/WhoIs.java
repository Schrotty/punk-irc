package de.rubenmaurer.punk.core.irc.messages;

public class WhoIs extends Message {
    private String nickname;
    private String realname;

    public String getNickname() {
        return nickname;
    }

    public String getRealname() {
        return realname;
    }

    WhoIs(String nickname) {
        this.nickname = nickname;
    }

    WhoIs(String nickname, String realname) {
        this.nickname = nickname;
        this.realname = realname;
        request = false;
    }

    WhoIs(String nickname, String realname, String message) {
        this(nickname, realname);
        this.errorMessage = message;
        this.error = true;
    }
}
