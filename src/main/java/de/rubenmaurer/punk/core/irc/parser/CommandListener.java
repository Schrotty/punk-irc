package de.rubenmaurer.punk.core.irc.parser;

import akka.actor.ActorRef;
import de.rubenmaurer.punk.IRCListener;
import de.rubenmaurer.punk.IRCParser;
import de.rubenmaurer.punk.core.irc.client.Connection;
import de.rubenmaurer.punk.core.irc.messages.MessageBuilder;
import de.rubenmaurer.punk.util.Notification;
import de.rubenmaurer.punk.util.Settings;
import de.rubenmaurer.punk.util.Template;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * CommandListener for performing actions based on the listened command.
 *
 * @author Ruben Maurer
 * @version 1.0
 * @since 1.0
 */
public class CommandListener implements IRCListener {

    /**
     * Connection for this listener
     */
    private Connection connection;

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
    CommandListener(Connection connection, ActorRef worker) {
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

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#chatLine}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitChatLine(IRCParser.ChatLineContext ctx) {
        worker.tell(connection, ActorRef.noSender());
    }

    /**
     * Enter a parse tree produced by {@link IRCParser#commands}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterCommands(IRCParser.CommandsContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#commands}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitCommands(IRCParser.CommandsContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#initCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterInitCommand(IRCParser.InitCommandContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#initCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitInitCommand(IRCParser.InitCommandContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#nickCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterNickCommand(IRCParser.NickCommandContext ctx) {
        connection.setNickname(ctx.user().getText());
    }

    /**
     * Exit a parse tree produced by {@link IRCParser#nickCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitNickCommand(IRCParser.NickCommandContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#userCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterUserCommand(IRCParser.UserCommandContext ctx) {
        connection.setRealname(ctx.realname().getText());
    }

    /**
     * Exit a parse tree produced by {@link IRCParser#userCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitUserCommand(IRCParser.UserCommandContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#capCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterCapCommand(IRCParser.CapCommandContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#capCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitCapCommand(IRCParser.CapCommandContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#quitCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterQuitCommand(IRCParser.QuitCommandContext ctx) {
        connection.logout(ctx.message() == null ? Template.get("quitMessage").toString() : ctx.message().getText());
    }

    /**
     * Exit a parse tree produced by {@link IRCParser#quitCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitQuitCommand(IRCParser.QuitCommandContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#privmsgCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPrivmsgCommand(IRCParser.PrivmsgCommandContext ctx) {
        connection.chat(ctx.user().getText(),
                ctx.message() == null ? "" : ctx.message().getText());
    }

    /**
     * Exit a parse tree produced by {@link IRCParser#privmsgCommand}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitPrivmsgCommand(IRCParser.PrivmsgCommandContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#ping}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPing(IRCParser.PingContext ctx) {
        connection.pong();
    }

    /**
     * Exit a parse tree produced by {@link IRCParser#ping}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitPing(IRCParser.PingContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#pong}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPong(IRCParser.PongContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#pong}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitPong(IRCParser.PongContext ctx) {

    }

    @Override
    public void enterMotd(IRCParser.MotdContext ctx) {
        connection.write(Settings.messageofTheDay());
    }

    @Override
    public void exitMotd(IRCParser.MotdContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#whois}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterWhois(IRCParser.WhoisContext ctx) {
        connection.getConnection().tell(MessageBuilder.whoIs(ctx.user().getText()), ActorRef.noSender());
    }

    /**
     * Exit a parse tree produced by {@link IRCParser#whois}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitWhois(IRCParser.WhoisContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#realname}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterRealname(IRCParser.RealnameContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#realname}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitRealname(IRCParser.RealnameContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#message}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterMessage(IRCParser.MessageContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#message}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitMessage(IRCParser.MessageContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#user}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterUser(IRCParser.UserContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#user}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitUser(IRCParser.UserContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#userMode}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterUserMode(IRCParser.UserModeContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#userMode}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitUserMode(IRCParser.UserModeContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#unused}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterUnused(IRCParser.UnusedContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#unused}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitUnused(IRCParser.UnusedContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#delimiter}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterDelimiter(IRCParser.DelimiterContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#delimiter}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitDelimiter(IRCParser.DelimiterContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {
        connection.write(Notification.get(Notification.Error.ERR_UNKNOWNCOMMAND, new String[] { "schrotty", errorNode.getText() }));
    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
