package com.stephenfox;

public class CompilerError extends RuntimeException {
  public CompilerError(String message) {
    super(message);
  }
}
