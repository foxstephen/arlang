package com.stephenfox;

import com.stephenfox.lang.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;

/**
 * Bytecode generation class for ArLang. Generates all bytecode as a parameter to the
 * System.out.println call in a main method. This ensure that all expression in the language are
 * outputted to stdout.
 *
 * @author Stephen Fox
 */
public class BytecodeGenerator {

  private final ClassWriter cw;
  private final String className;
  private MethodVisitor mv;
  private int localsIndex = 0;

  public BytecodeGenerator(String className) {
    this.className = className;
    this.cw = new ClassWriter(COMPUTE_MAXS);
  }

  /**
   * Starts bytecode generation. This method writes the start of the class file, i.e. the main
   * method and loads the static field System.out onto the JVM stack.
   */
  public void start() {
    cw.visit(V1_5, ACC_PUBLIC, className, null, "java/lang/Object", null);
    mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
    mv.visitCode();
  }

  /**
   * End bytecode generation. This method writes the end of the class file, i.e invokes println on
   * the System.out field. This outputs the result of the expression in ArLang to stdout.
   */
  public void end() {


  }

  public byte[] getBytes() {
    return cw.toByteArray();
  }

  /**
   * Writes the binary operator to the class file.
   *
   * @param binOp The binary operator to use.
   */
  void writeBinaryOperator(BinaryOperator binOp) {
    pushNumberOntoStack(mv, binOp.lhs());
    pushNumberOntoStack(mv, binOp.rhs());

    switch (binOp.operator()) {
      case ADD:
        mv.visitInsn(IADD);
        break;
      case SUB:
        mv.visitInsn(ISUB);
        break;
      case DIV:
        mv.visitInsn(IDIV);
        break;
      case MUL:
        mv.visitInsn(IMUL);
        break;
      case GT:
        mv.visitInsn(IF_ICMPGT);
        break;
      case LT:
        mv.visitInsn(IF_ICMPLT);
      default:
        throw new IllegalArgumentException("Unknown operand");
    }
  }

  void writeForLoop(ForLoop forLoop) {
    ArLangNumber fromNumber = forLoop.binaryOperator().lhs();
    ArLangNumber toNumber = forLoop.binaryOperator().rhs();

    pushNumberOntoStack(mv, fromNumber);
    mv.visitVarInsn(ISTORE, ++localsIndex);
    Label loopBackLabel = new Label();
    mv.visitLabel(loopBackLabel);
    mv.visitVarInsn(ILOAD, localsIndex);
    pushNumberOntoStack(mv, toNumber);
    Label end = new Label();

    // Decide how to jump.
    // 1 < 5  1
    //        2
    //        3
    //        4
    Operator operator = forLoop.binaryOperator().operator();
    if (operator == Operator.LT) {
      mv.visitJumpInsn(IF_ICMPGE, end);
    } else if (operator == Operator.LTE) {
      mv.visitJumpInsn(IF_ICMPGT, end);
    } else if (operator == Operator.GT) {
      mv.visitJumpInsn(IF_ICMPLE, end);
    } else if (operator == Operator.GTE) {
      mv.visitJumpInsn(IF_ICMPLT, end);
    } else {
      throw new BytecodeGenerationException("Invalid foor loop");
    }

    mv.visitIincInsn(localsIndex, localsIndex);
    mv.visitJumpInsn(GOTO, loopBackLabel);
    mv.visitLabel(end);
  }

  void writePrint(Print print) {
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    pushNumberOntoStack(mv, print.number());
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
  }

  /**
   * Puts the int onto the JVM stack inside some method.
   *
   * @param mv     The method visitor, the integer will be pushed onto the JVM stack inside this method.
   * @param number The value to push onto the JVM stack.
   */
  private void pushNumberOntoStack(MethodVisitor mv, ArLangNumber number) {
    if (canUseBiPush(number.value())) {
      mv.visitIntInsn(BIPUSH, number.value());
    } else if (canUseSiPush(number.value())) {
      mv.visitIntInsn(SIPUSH, number.value());
    } else {
      throw new CompilerError("Currently unsupported number range");
    }
  }


  private static boolean canUseBiPush(int value) {
    return value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE;
  }

  private static boolean canUseSiPush(int value) {
    return value <= Short.MAX_VALUE && value >= Short.MIN_VALUE;
  }
}
