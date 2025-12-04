package io.github.shawn.antlr.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Relation implements Rule {
  public String type = "relation";
  public String relation;        // "and" 或 "or"
  public List<Rule> rules;

  public Relation() {
  }

  public Relation(String relation, List<Rule> rules) {
    this.relation = relation;
    this.rules = rules;
  }
}
