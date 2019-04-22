package com.stephenfox;

import static com.stephenfox.operators.BinaryOperator.Type.ADD;
import static com.stephenfox.operators.BinaryOperator.Type.DIV;
import static com.stephenfox.operators.BinaryOperator.Type.MUL;
import static com.stephenfox.operators.BinaryOperator.Type.SUB;

import com.stephenfox.operators.BinaryOperator;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ArLangListenerImpl implements ArLangListener {

  private final BytecodeGenerator bytecodeGenerator;

  public ArLangListenerImpl(BytecodeGenerator bytecodeGenerator) {
    this.bytecodeGenerator = bytecodeGenerator;
  }

  @Override
  public void enterExpr(ArLangParser.ExprContext ctx) {
    bytecodeGenerator.start();
  }

  @Override
  public void exitExpr(ArLangParser.ExprContext ctx) {
    bytecodeGenerator.end();
  }

  @Override
  public void enterAdd(ArLangParser.AddContext ctx) {
    int lhs = Integer.parseInt(ctx.NUMBER(0).getText());
    int rhs = Integer.parseInt(ctx.NUMBER(1).getText());
    bytecodeGenerator.writeBinaryOperator(new BinaryOperator(ADD, lhs, rhs));
  }

  @Override
  public void exitAdd(ArLangParser.AddContext ctx) {
    // Not needed ignore.
  }

  @Override
  public void enterSub(ArLangParser.SubContext ctx) {
    int lhs = Integer.parseInt(ctx.NUMBER(0).getText());
    int rhs = Integer.parseInt(ctx.NUMBER(1).getText());
    bytecodeGenerator.writeBinaryOperator(new BinaryOperator(SUB, lhs, rhs));
  }

  @Override
  public void exitSub(ArLangParser.SubContext ctx) {
    // Not needed ignore.
  }

  @Override
  public void enterDiv(ArLangParser.DivContext ctx) {
    int lhs = Integer.parseInt(ctx.NUMBER(0).getText());
    int rhs = Integer.parseInt(ctx.NUMBER(1).getText());
    bytecodeGenerator.writeBinaryOperator(new BinaryOperator(DIV, lhs, rhs));
  }

  @Override
  public void exitDiv(ArLangParser.DivContext ctx) {
    // Not needed ignore.
  }

  @Override
  public void enterMul(ArLangParser.MulContext ctx) {
    int lhs = Integer.parseInt(ctx.NUMBER(0).getText());
    int rhs = Integer.parseInt(ctx.NUMBER(1).getText());
    bytecodeGenerator.writeBinaryOperator(new BinaryOperator(MUL, lhs, rhs));
  }

  @Override
  public void exitMul(ArLangParser.MulContext ctx) {
    // Not needed ignore.
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    // Not needed ignore.
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    // Not needed ignore.
  }

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {
    // Not needed ignore.
  }

  @Override
  public void exitEveryRule(ParserRuleContext ctx) {
    // Not needed ignore.
  }
}
