package com.stephenfox;

import com.stephenfox.lang.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ArLangVisitorImpl extends ArLangBaseVisitor<byte[]> {
  private static final int ALLOW_ASM_TO_COMPUTE_MAX_STACK = 0;
  private final ClassWriter cw;
  private final MethodVisitor mv;

  // Locals index starts at 1, as `this` is always at 0 position.
  // https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-2.html#jvms-2.6.1
  private int localsIndex = 0;

  public ArLangVisitorImpl(String className) {
    this.cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    cw.visit(V1_5, ACC_PUBLIC, className, null, "java/lang/Object", null);

    // By default everything will be written inside main method.
    this.mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
    this.mv.visitCode();
  }

  @Override
  public byte[] visitProgram(ArLangParser.ProgramContext ctx) {
    new ProgramVisitor().visitProgram(ctx);
    mv.visitInsn(RETURN);
    mv.visitMaxs(ALLOW_ASM_TO_COMPUTE_MAX_STACK, localsIndex + 1);
    mv.visitEnd();
    cw.visitEnd();
    return cw.toByteArray();
  }

  private class ProgramVisitor extends ArLangBaseVisitor<Void> {
    @Override
    public Void visitProgram(ArLangParser.ProgramContext ctx) {
      if (is(ctx.expression())) {
        new ExpressionVisitor().visitExpression(ctx.expression());
      } else if (is(ctx.statement())) {
        new StatementVisitor().visitStatement(ctx.statement());
      } else {
        throw new CompilerError("Unknown program");
      }
      return super.visitProgram(ctx);
    }
  }

  private class StatementVisitor extends ArLangBaseVisitor<Void> {
    @Override
    public Void visitStatement(ArLangParser.StatementContext ctx) {
      if (is(ctx.forLoop())) {
        new ForLoopVisitor().visitForLoop(ctx.forLoop());
      } else {
        throw new CompilerError("Unknown statement");
      }
      return null;
    }
  }


  private class ForLoopVisitor extends ArLangBaseVisitor<Void> {
    @Override
    public Void visitForLoop(ArLangParser.ForLoopContext ctx) {
      BinaryOperator binaryOperator = new BinaryOperatorVisitor().visit(ctx.expression());
      Operator operator = binaryOperator.operator();
      mv.visitIntInsn(BIPUSH, binaryOperator.lhs().value());
      mv.visitVarInsn(ISTORE, ++localsIndex);
      Label loopBackLabel = new Label();
      mv.visitLabel(loopBackLabel);
      mv.visitVarInsn(ILOAD, localsIndex);
      mv.visitIntInsn(BIPUSH, binaryOperator.rhs().value());

      Label terminalLabel = new Label();


      if (operator == Operator.LT) {
        mv.visitJumpInsn(IF_ICMPGE, terminalLabel);
      } else if (operator == Operator.LTE) {
        mv.visitJumpInsn(IF_ICMPGT, terminalLabel);
      } else if (operator == Operator.GT) {
        mv.visitJumpInsn(IF_ICMPLE, terminalLabel);
      } else if (operator == Operator.GTE) {
        mv.visitJumpInsn(IF_ICMPLT, terminalLabel);
      } else {
        throw new BytecodeGenerationException("Invalid for loop");
      }

      // Assume we can only print for the time being.
      new PrintVisitor().visitPrint(ctx.statement().print());

      mv.visitIincInsn(localsIndex, 1);
      mv.visitJumpInsn(GOTO, loopBackLabel);

      mv.visitLabel(terminalLabel);

      return null;
    }
  }

  private static class ExpressionVisitor extends ArLangBaseVisitor<Expression> {
    @Override
    public Expression visitExpression(ArLangParser.ExpressionContext ctx) {
      return super.visitExpression(ctx);
    }
  }

  private static class BinaryOperatorVisitor extends ArLangBaseVisitor<BinaryOperator> {
    @Override
    public BinaryOperator visitBinaryOperator(ArLangParser.BinaryOperatorContext ctx) {
      ArLangNumber lhs = new ArLangNumber(ctx.NUMBER(0).getText());
      ArLangNumber rhs = new ArLangNumber(ctx.NUMBER(1).getText());

      Operator operator;

      if (is(ctx.LT())) {
        operator = Operator.LT;
      } else if (is(ctx.LTE())) {
        operator = Operator.LTE;
      } else if (is(ctx.GT())) {
        operator = Operator.GT;
      } else if (is(ctx.GTE())) {
        operator = Operator.GTE;
      } else if (is(ctx.SUB())) {
        operator = Operator.SUB;
      } else if (is(ctx.DIV())) {
        operator = Operator.DIV;
      } else if (is(ctx.MUL())) {
        operator = Operator.MUL;
      } else if (is(ctx.ADD())) {
        operator = Operator.ADD;
      } else {
        throw new CompilerError("Unknown operator for binary expression");
      }

      return new BinaryOperator(operator, lhs, rhs);
    }
  }

  private class PrintVisitor extends ArLangBaseVisitor<Void> {
    @Override
    public Void visitPrint(ArLangParser.PrintContext ctx) {
      ArLangNumber number = new ArLangNumber(ctx.NUMBER().getText());
      mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      mv.visitIntInsn(SIPUSH, number.value());
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
      return null;
    }
  }

  private static boolean is(TerminalNode context) {
    return context != null;
  }

  private boolean is(ParserRuleContext context) {
    return context != null;
  }
}
