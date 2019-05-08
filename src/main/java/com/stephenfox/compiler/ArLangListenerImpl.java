package com.stephenfox.compiler;

import com.stephenfox.ArLangBaseListener;
import com.stephenfox.ArLangParser;
import com.stephenfox.bytecode.BytecodeGenerator;

import org.antlr.v4.runtime.tree.ErrorNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArLangListenerImpl extends ArLangBaseListener {

  private final BytecodeGenerator bytecode;
  // This is hack until I figure out how to stop
  // UnaryExpr and Int from both match an int :/
  private boolean skipNextEnterIntBecauseOfUnary = false;

  ArLangListenerImpl(BytecodeGenerator bytecode) {
    this.bytecode = bytecode;
  }

  @Override
  public void enterArlang(ArLangParser.ArlangContext ctx) {
    LOGGER.trace("Entered ArLang");
    bytecode.start();
  }

  @Override
  public void exitArlang(ArLangParser.ArlangContext ctx) {
    LOGGER.trace("Exited ArLang");
    bytecode.end();
  }

  @Override
  public void exitDiv(ArLangParser.DivContext ctx) {
    LOGGER.trace("Writing {}", ctx.getText());
    bytecode.writeDiv();
  }

  @Override
  public void exitAdd(ArLangParser.AddContext ctx) {
    LOGGER.trace("Writing {}", ctx.getText());
    bytecode.writeAdd();
  }

  @Override
  public void exitMul(ArLangParser.MulContext ctx) {
    LOGGER.trace("Writing {}", ctx.getText());
    bytecode.writeMul();
  }

  @Override
  public void exitSub(ArLangParser.SubContext ctx) {
    LOGGER.trace("Writing {}", ctx.getText());
    bytecode.writeSub();
  }

  @Override
  public void enterInt(ArLangParser.IntContext ctx) {
    if (!skipNextEnterIntBecauseOfUnary) {
      LOGGER.trace("Writing {}", ctx.getText());
      final int i = Integer.parseInt(ctx.INT().getText());
      bytecode.writeInt(i);
    }
    skipNextEnterIntBecauseOfUnary = false;
  }

  @Override
  public void enterFloat(ArLangParser.FloatContext ctx) {
    LOGGER.trace("Writing {}", ctx.getText());
  }

  @Override
  public void enterUnaryExpr(ArLangParser.UnaryExprContext ctx) {
    LOGGER.trace("Writing {}", ctx.getText());
    final int i = Integer.parseInt(ctx.getText());
    bytecode.writeInt(i);
    skipNextEnterIntBecauseOfUnary = true;
  }

  @Override
  public void visitErrorNode(ErrorNode node) {
    throw new ArlangCompilerError(node.getText());
  }
}
