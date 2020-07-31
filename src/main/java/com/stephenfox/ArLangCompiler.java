package com.stephenfox;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ArLangCompiler {

  private final String text;

  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      System.out.println(
              "Please specify the following args: <program>\n" + "i.e. java -jar arlang-compiler.jar <filename of program>");
      System.exit(1);
    }
    System.out.print("Compiler options=");
    System.out.println(Arrays.toString(args));
    String programFile = args[0];
    Path of = Path.of(programFile);
    String program = new String(Files.readAllBytes(of.toAbsolutePath()));
    System.out.println("Compiling program...");
    System.out.println(program);
    new ArLangCompiler(program).compile();
  }

  public ArLangCompiler(String text) {
    this.text = text;
  }

  public void compile() throws IOException {
    // A stream of characters that makes up a program in ArLang.
    CharStream charStream = CharStreams.fromString(text);
    // Run the lexer over the character stream input.
    ArLangLexer lexer = new ArLangLexer(charStream);

    // Generate a token stream from the lexer for the parse to use.
    TokenStream tokens = new CommonTokenStream(lexer);
    ArLangParser parser = new ArLangParser(tokens);

    ArLangVisitorImpl arLangVisitor = new ArLangVisitorImpl("Program");
    byte[] byteCode = arLangVisitor.visitProgram(parser.program());

    final Path path = Path.of(FileSystems.getDefault().getPath(".").toString(), "Program.class");

    try (FileOutputStream outputStream = new FileOutputStream(path.toString())) {
      outputStream.write(byteCode);
    }
  }
}
