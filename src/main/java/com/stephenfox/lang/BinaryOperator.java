package com.stephenfox.lang;

public class BinaryOperator implements Expression {

  private final Operator operator;
  private final ArLangNumber lhs;
  private final ArLangNumber rhs;

  public BinaryOperator(Operator operator, ArLangNumber lhs, ArLangNumber rhs) {
    this.operator = operator;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public ArLangNumber lhs() {
    return lhs;
  }

  public ArLangNumber rhs() {
    return rhs;
  }

  public Operator operator() {
    return operator;
  }
}
