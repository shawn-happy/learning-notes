package io.github.shawn.antlr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.shawn.antlr.domain.Relation;
import io.github.shawn.antlr.domain.Rule;
import io.github.shawn.antlr.domain.SingleRule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonRuleVisitor extends LabelRuleBaseVisitor<Rule> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Rule visitParse(LabelRuleParser.ParseContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Rule visitAndExpr(LabelRuleParser.AndExprContext ctx) {
        Rule left = visit(ctx.expression(0));
        Rule right = visit(ctx.expression(1));
        return new Relation("and", Arrays.asList(left, right));
    }

    @Override
    public Rule visitOrExpr(LabelRuleParser.OrExprContext ctx) {
        Rule left = visit(ctx.expression(0));
        Rule right = visit(ctx.expression(1));
        return new Relation("or", Arrays.asList(left, right));
    }

    @Override
    public Rule visitParenExpr(LabelRuleParser.ParenExprContext ctx) {
        return visit(ctx.expression());
    }

    @Override
    public Rule visitBinaryOpAtom(LabelRuleParser.BinaryOpAtomContext ctx) {
        String tagType = ctx.field().tagType().getText();
        String tagCode = ctx.field().tagCode().getText();
        String op = mapOperator(ctx.op().getText());
        List<Object> params = new ArrayList<>();
        params.add(cleanValue(ctx.value().getText()));
        return new SingleRule(tagType, tagCode, op, params);
    }

    @Override
    public Rule visitInOpAtom(LabelRuleParser.InOpAtomContext ctx) {
        String tagType = ctx.field().tagType().getText();
        String tagCode = ctx.field().tagCode().getText();
        List<Object> params = new ArrayList<>();
        for (LabelRuleParser.ValueContext v : ctx.valueList().value()) {
            params.add(cleanValue(v.getText()));
        }
        return new SingleRule(tagType, tagCode, "in", params);
    }


    private String mapOperator(String op) {
        switch (op) {
            case ">": return "gt";
            case ">=": return "gte";
            case "<": return "lt";
            case "<=": return "lte";
            case "=":
            case "==": return "eq";
            case "!=": return "neq";
            default: return "eq";
        }
    }

    private Object cleanValue(String text) {
        if (text.startsWith("'") && text.endsWith("'") && text.length() >= 2) {
            // 去掉首尾单引号，支持字符串
            return text.substring(1, text.length() - 1);
        }
        // 尝试转换数字，防止字符串数字污染
        try {
            if (text.contains(".")) {
                return Double.parseDouble(text);
            } else {
                return Integer.parseInt(text);
            }
        } catch (NumberFormatException e) {
            // 不是数字直接返回原字符串
            return text;
        }
    }
}

