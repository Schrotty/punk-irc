package de.rubenmaurer.punk.core.irc.parser;

import akka.actor.ActorRef;
import de.rubenmaurer.punk.IRCBaseListener;
import de.rubenmaurer.punk.IRCParser;
import de.rubenmaurer.punk.core.irc.messages.MessageBuilder;
import de.rubenmaurer.punk.core.irc.messages.impl.Change;
import de.rubenmaurer.punk.core.irc.messages.impl.Chat;
import de.rubenmaurer.punk.util.Notification;
import de.rubenmaurer.punk.util.Settings;
import de.rubenmaurer.punk.util.Template;
import org.apache.commons.lang3.StringUtils;

/**
 * CommandListener for performing actions based on the listened command.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class CommandListener extends IRCBaseListener {

    /**
     * Connection for this listener
     */
    private ActorRef connection;

    /**
     * The worker this worker works for.
     */
    private ActorRef worker;

    /**
     * Create new command listener.
     *
     * @param connection the used connection
     * @param worker the used worker
     */
    CommandListener(ActorRef connection, ActorRef worker) {
        this.connection = connection;
        this.worker = worker;
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#chatLine}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterChatLine(IRCParser.ChatLineContext ctx) {
        if (ctx.commands().size() == 0) {
            if (StringUtils.isAllUpperCase(ctx.WORD(0).getText())) {
                connection.tell(Notification.get(Notification.Error.ERR_UNKNOWNCOMMAND,
                        new String[]{Settings.hostname(), ctx.WORD(0).getText() }), worker);
            }
        }
    }

    /**
     * Exit a parse tree produced by {@link IRCParser#chatLine}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitChatLine(IRCParser.ChatLineContext ctx) {
        worker.tell(Template.get("workerDone").toString(), ActorRef.noSender());
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#nickCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterNickCommand(IRCParser.NickCommandContext ctx) {
        connection.tell(MessageBuilder.change(Change.Field.NICKNAME, ctx.user().getText()), worker);
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#userCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterUserCommand(IRCParser.UserCommandContext ctx) {
        connection.tell(MessageBuilder.change(Change.Field.REALNAME, ctx.user().getText()), worker);
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#quitCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterQuitCommand(IRCParser.QuitCommandContext ctx) {
        connection.tell(MessageBuilder.logout(
                ctx.message() == null ? Template.get("quitMessage").toString() : ctx.message().getText()
        ), worker);
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#privmsgCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPrivmsgCommand(IRCParser.PrivmsgCommandContext ctx) {
        if (ctx.user().getText().startsWith("#")) {
            connection.tell(MessageBuilder.chat(ctx.user().getText(), Chat.TargetType.CHANNEL, ctx.message() == null ? "" : ctx.message().getText(), Chat.Type.PRIVMSG), worker);
            return;
        }

        connection.tell(MessageBuilder.chat(ctx.user().getText(), Chat.TargetType.USER, ctx.message() == null ? "" : ctx.message().getText(), Chat.Type.PRIVMSG), worker);
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#notice}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterNotice(IRCParser.NoticeContext ctx) {
        if (ctx.user().getText().startsWith("#")) {
            connection.tell(MessageBuilder.chat(ctx.user().getText(), Chat.TargetType.CHANNEL, ctx.message() == null ? "" : ctx.message().getText(), Chat.Type.NOTICE), worker);
            return;
        }

        connection.tell(MessageBuilder.chat(ctx.user().getText(), Chat.TargetType.USER, ctx.message() == null ? "" : ctx.message().getText(), Chat.Type.NOTICE), worker);
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#ping}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPing(IRCParser.PingContext ctx) {
        connection.tell(Template.get("pong").toString(), worker);
    }

    @Override
    public void enterMotd(IRCParser.MotdContext ctx) {
        connection.tell(Template.get("motd").toString(), worker);
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#whois}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterWhois(IRCParser.WhoisContext ctx) {
        connection.tell(MessageBuilder.whoIs(ctx.user().getText()), worker);
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#join}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterJoin(IRCParser.JoinContext ctx) {
        //connection.join(ctx.WORD().getText());/
        connection.tell(MessageBuilder.join(ctx.WORD().getText()), worker);
    }
}
