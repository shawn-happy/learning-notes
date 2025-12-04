package io.github.shawn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.shawn.antlr.JsonRuleVisitor;
import io.github.shawn.antlr.LabelRuleLexer;
import io.github.shawn.antlr.LabelRuleParser;
import io.github.shawn.antlr.domain.Rule;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonBuildVisitorV2Test {

  @Test
  public void testJsonBuild() throws Exception {
    String input = "realtime.login_cnt > 5 AND (offline.gender = 'male' OR offline.age IN (18, 19, 20))";

    LabelRuleLexer lexer = new LabelRuleLexer(CharStreams.fromString(input));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    LabelRuleParser parser = new LabelRuleParser(tokens);

    LabelRuleParser.ParseContext tree = parser.parse();

    JsonRuleVisitor visitor = new JsonRuleVisitor();
    Rule result = visitor.visit(tree);

    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);

    String expectedJson =
        "{\n" +
            "  \"type\" : \"relation\",\n" +
            "  \"relation\" : \"and\",\n" +
            "  \"rules\" : [ {\n" +
            "    \"type\" : \"rule\",\n" +
            "    \"tagType\" : \"realtime\",\n" +
            "    \"tagCode\" : \"login_cnt\",\n" +
            "    \"op\" : \"gt\",\n" +
            "    \"params\" : [ 5 ]\n" +
            "  }, {\n" +
            "    \"type\" : \"relation\",\n" +
            "    \"relation\" : \"or\",\n" +
            "    \"rules\" : [ {\n" +
            "      \"type\" : \"rule\",\n" +
            "      \"tagType\" : \"offline\",\n" +
            "      \"tagCode\" : \"gender\",\n" +
            "      \"op\" : \"eq\",\n" +
            "      \"params\" : [ \"male\" ]\n" +
            "    }, {\n" +
            "      \"type\" : \"rule\",\n" +
            "      \"tagType\" : \"offline\",\n" +
            "      \"tagCode\" : \"age\",\n" +
            "      \"op\" : \"in\",\n" +
            "      \"params\" : [ 18, 19, 20 ]\n" +
            "    } ]\n" +
            "  } ]\n" +
            "}";

    // 比较格式化的字符串，确保结构匹配
    assertEquals(mapper.readTree(expectedJson), mapper.readTree(json));
  }
}