package io.github.shawn.antlr;// Generated from LabelRule.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LabelRuleParser}.
 */
public interface LabelRuleListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LabelRuleParser#parse}.
	 * @param ctx the parse tree
	 */
	void enterParse(LabelRuleParser.ParseContext ctx);
	/**
	 * Exit a parse tree produced by {@link LabelRuleParser#parse}.
	 * @param ctx the parse tree
	 */
	void exitParse(LabelRuleParser.ParseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(LabelRuleParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(LabelRuleParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(LabelRuleParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(LabelRuleParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AtomExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpr(LabelRuleParser.AtomExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AtomExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpr(LabelRuleParser.AtomExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(LabelRuleParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(LabelRuleParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryOpAtom}
	 * labeled alternative in {@link LabelRuleParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOpAtom(LabelRuleParser.BinaryOpAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryOpAtom}
	 * labeled alternative in {@link LabelRuleParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOpAtom(LabelRuleParser.BinaryOpAtomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InOpAtom}
	 * labeled alternative in {@link LabelRuleParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterInOpAtom(LabelRuleParser.InOpAtomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InOpAtom}
	 * labeled alternative in {@link LabelRuleParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitInOpAtom(LabelRuleParser.InOpAtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link LabelRuleParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(LabelRuleParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link LabelRuleParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(LabelRuleParser.FieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link LabelRuleParser#tagType}.
	 * @param ctx the parse tree
	 */
	void enterTagType(LabelRuleParser.TagTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LabelRuleParser#tagType}.
	 * @param ctx the parse tree
	 */
	void exitTagType(LabelRuleParser.TagTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LabelRuleParser#tagCode}.
	 * @param ctx the parse tree
	 */
	void enterTagCode(LabelRuleParser.TagCodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LabelRuleParser#tagCode}.
	 * @param ctx the parse tree
	 */
	void exitTagCode(LabelRuleParser.TagCodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LabelRuleParser#valueList}.
	 * @param ctx the parse tree
	 */
	void enterValueList(LabelRuleParser.ValueListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LabelRuleParser#valueList}.
	 * @param ctx the parse tree
	 */
	void exitValueList(LabelRuleParser.ValueListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LabelRuleParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(LabelRuleParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link LabelRuleParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(LabelRuleParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link LabelRuleParser#op}.
	 * @param ctx the parse tree
	 */
	void enterOp(LabelRuleParser.OpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LabelRuleParser#op}.
	 * @param ctx the parse tree
	 */
	void exitOp(LabelRuleParser.OpContext ctx);
}