package de.rubenmaurer.punk.core.listener;

import de.rubenmaurer.punk.IRCListener;
import de.rubenmaurer.punk.IRCParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class CommandListener implements IRCListener {

    /**
     * Enter a parse tree produced by {@link IRCParser#chat}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterChat(IRCParser.ChatContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#chat}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitChat(IRCParser.ChatContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#line}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterLine(IRCParser.LineContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#line}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitLine(IRCParser.LineContext ctx) {

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
     * Enter a parse tree produced by {@link IRCParser#name}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterName(IRCParser.NameContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#name}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitName(IRCParser.NameContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#command}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterCommand(IRCParser.CommandContext ctx) {
        System.out.println(ctx.WORD());
    }

    /**
     * Exit a parse tree produced by {@link IRCParser#command}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitCommand(IRCParser.CommandContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#emoticon}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterEmoticon(IRCParser.EmoticonContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#emoticon}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitEmoticon(IRCParser.EmoticonContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#link}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterLink(IRCParser.LinkContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#link}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitLink(IRCParser.LinkContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#color}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterColor(IRCParser.ColorContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#color}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitColor(IRCParser.ColorContext ctx) {

    }

    /**
     * Enter a parse tree produced by {@link IRCParser#mention}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterMention(IRCParser.MentionContext ctx) {

    }

    /**
     * Exit a parse tree produced by {@link IRCParser#mention}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitMention(IRCParser.MentionContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
