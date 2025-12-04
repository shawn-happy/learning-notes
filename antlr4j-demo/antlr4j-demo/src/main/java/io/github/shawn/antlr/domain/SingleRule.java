package io.github.shawn.antlr.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleRule implements Rule {
  public String type = "rule";
  public String tagType;
  public String tagCode;
  public String op;
  public List<Object> params;  // 支持字符串或数字

  public SingleRule() {
  }

  public SingleRule(String tagType, String tagCode, String op, List<Object> params) {
    this.tagType = tagType;
    this.tagCode = tagCode;
    this.op = op;
    this.params = params;
  }
}
