package com.stephenfox.bytecode;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ICONST_0;
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

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import lombok.extern.slf4j.Slf4j;

/**
 * Bytecode generation class for ArLang. Generates all bytecode as a parameter to the
 * System.out.println call in a main method. This ensure that all expression in the language are
 * outputted to stdout.
 *
 * @author Stephen Fox
 */
@Slf4j
public class BytecodeGenerator {
  private final ClassWriter cw = new ClassWriter(0);
  private final String className;
  private MethodVisitor main;
  private int mainStackSize = 0;

  public BytecodeGenerator(String className) {
    this.className = className;
  }

  /**
   * Starts bytecode generation. This method writes the start of the class file, i.e. the main
   * method and loads the static field System.out onto the JVM stack.
   */
  public void start() {
    LOGGER.debug("Bytecode generation started for classname {}", className);
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
    LOGGER.debug("Bytecode generation ended for classname {}", className);
    main.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
    main.visitInsn(RETURN);
    main.visitMaxs(mainStackSize, 1);
    main.visitEnd();
    cw.visitEnd();
  }

  public byte[] getBytes() {
    return cw.toByteArray();
  }

  private void incrementStackSize() {
    mainStackSize++;
  }

  /**
   * Writes an int to the stack.
   *
   * @param value Some integer value.
   */
  public void writeInt(int value) {
    incrementStackSize();
    writeInt(main, value);
  }

  private void writeInt(MethodVisitor method, int i) {
    if (i == -1) {
      method.visitInsn(ICONST_M1);
    } else if (i == 0) {
      method.visitInsn(ICONST_0);
    } else if (i == 1) {
      method.visitInsn(ICONST_1);
    } else if (i == 2) {
      method.visitInsn(ICONST_2);
    } else if (i == 3) {
      method.visitInsn(ICONST_3);
    } else if (i == 4) {
      method.visitInsn(ICONST_4);
    } else if (i == 5) {
      method.visitInsn(ICONST_5);
    } else {
      final int cIndex = cw.newConst(i);
      method.visitInsn(LDC);
      method.visitInsn(cIndex);
    }
  }

  public void writeMul() {
    main.visitInsn(IMUL);
  }

  public void writeAdd() {
    main.visitInsn(IADD);
  }

  public void writeSub() {
    main.visitInsn(ISUB);
  }

  public void writeDiv() {
    main.visitInsn(IDIV);
  }
}
