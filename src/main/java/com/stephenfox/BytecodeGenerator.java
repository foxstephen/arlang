package com.stephenfox;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.ICONST_M1;
import static org.objectweb.asm.Opcodes.IDIV;
import static org.objectweb.asm.Opcodes.IMUL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISUB;
import static org.objectweb.asm.Opcodes.LDC;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V11;

import com.stephenfox.operators.BinaryOperator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

/**
 * Bytecode generation class for ArLang. Generates all bytecode as a parameter to the
 * System.out.println call in a main method. This ensure that all expression in the language are
 * outputted to stdout.
 *
 * @author Stephen Fox
 */
public class BytecodeGenerator {
  private final ClassWriter cw = new ClassWriter(0);
  private final String className;
  private MethodVisitor main;

  public BytecodeGenerator(String className) {
    this.className = className;
  }

  /**
   * Starts bytecode generation. This method writes the start of the class file, i.e. the main
   * method and loads the static field System.out onto the JVM stack.
   */
  public void start() {
    cw.visit(V11, ACC_PUBLIC, className, null, "java/lang/Object", null);
    main = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
    main.visitCode();
    main.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
  }

  /**
   * End bytecode generation. This method writes the end of the class file, i.e invokes println on
   * the System.out field. This outputs the result of the expression in ArLang to stdout.
   */
  public void end() {
    main.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
    main.visitInsn(RETURN);
    main.visitMaxs(3, 1);
    main.visitEnd();
    cw.visitEnd();
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
    intOpcode(main, binOp.getLhs());
    intOpcode(main, binOp.getRhs());


    switch (binOp.getType()) {
      case ADD:
        main.visitInsn(IADD);
        break;
      case SUB:
        main.visitInsn(ISUB);
        break;
      case DIV:
        main.visitInsn(IDIV);
        break;
      case MUL:
        main.visitInsn(IMUL);
        break;
      default:
        throw new IllegalArgumentException("Unknown operand");
    }
  }

  /**
   * Puts the int onto the JVM stack inside some method.
   *
   * @param mv The method visitor, the integer will be pushed onto the JVM stack inside this method.
   * @param value The value to push onto the JVM stack.
   */
  private void intOpcode(MethodVisitor mv, int value) {
    if (value < 6) {
      if (value == -1) {
        mv.visitInsn(ICONST_M1);
      } else if (value == 1) {
        mv.visitInsn(ICONST_1);
      } else if (value == 2) {
        mv.visitInsn(ICONST_2);
      } else if (value == 3) {
        mv.visitInsn(ICONST_3);
      } else if (value == 4) {
        mv.visitInsn(ICONST_4);
      } else if (value == 5) {
        mv.visitInsn(ICONST_5);
      }
      return;
    }

    final int cIndex = cw.newConst(value);
    mv.visitInsn(LDC);
    mv.visitInsn(cIndex);
  }
}
