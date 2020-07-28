package com.stephenfox.lang;

import com.stephenfox.CompilerError;

public class ForLoop implements ArLangConstruct {
  private final BinaryOperator binaryOperator;

  public ForLoop(BinaryOperator binaryOperator, Print print) {
    if (binaryOperator.operator() != Operator.LT &&
            binaryOperator.operator() != Operator.GT) {
      throw new CompilerError("Invalid operator for for-loop");
    }

    this.binaryOperator = binaryOperator;
  }

  public BinaryOperator binaryOperator() {
    return binaryOperator;
  }
}
