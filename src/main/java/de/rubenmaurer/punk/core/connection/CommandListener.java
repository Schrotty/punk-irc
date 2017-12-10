package de.rubenmaurer.punk.core.connection;

import de.rubenmaurer.punk.IRCListener;
import de.rubenmaurer.punk.IRCParser;
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
     * Create new command listener.
     *
     * @param connection the used connection
     */
    public CommandListener(Connection connection) {
        this.connection = connection;
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

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {
        connection.write(Template.get("ERR_UNKNOWNCOMMAND").multiple(
                new String[] {"client", "command"},
                new String[] {"schrotty", errorNode.getText()})
        );
    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
