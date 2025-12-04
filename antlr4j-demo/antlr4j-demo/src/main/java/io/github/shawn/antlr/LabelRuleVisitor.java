package io.github.shawn.antlr;// Generated from LabelRule.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LabelRuleParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LabelRuleVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LabelRuleParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(LabelRuleParser.ParseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(LabelRuleParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(LabelRuleParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AtomExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomExpr(LabelRuleParser.AtomExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link LabelRuleParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(LabelRuleParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryOpAtom}
	 * labeled alternative in {@link LabelRuleParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryOpAtom(LabelRuleParser.BinaryOpAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InOpAtom}
	 * labeled alternative in {@link LabelRuleParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInOpAtom(LabelRuleParser.InOpAtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link LabelRuleParser#field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(LabelRuleParser.FieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link LabelRuleParser#tagType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagType(LabelRuleParser.TagTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LabelRuleParser#tagCode}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTagCode(LabelRuleParser.TagCodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LabelRuleParser#valueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueList(LabelRuleParser.ValueListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LabelRuleParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(LabelRuleParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link LabelRuleParser#op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOp(LabelRuleParser.OpContext ctx);
}