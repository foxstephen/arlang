package com.stephenfox.compiler;

/**
 * Indicates a syntactic compile time error.
 *
 * @author Stephen Fox.
 */
public class ArlangCompilerError extends Error {

  public ArlangCompilerError(String message) {
    super(message);
  }
}
