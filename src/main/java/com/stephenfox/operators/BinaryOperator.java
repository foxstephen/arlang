package com.stephenfox.operators;

import com.stephenfox.ArLangParser;

public class BinaryOperator {
  public enum Type {
    ADD,
    SUB,
    DIV,
    MUL
  }

  private final Type type;
  private final int lhs;
  private final int rhs;

  public BinaryOperator(Type type, int lhs, int rhs) {
    this.type = type;
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public int getLhs() {
    return lhs;
  }

  public int getRhs() {
    return rhs;
  }

  public Type getType() {
    return type;
  }
}
