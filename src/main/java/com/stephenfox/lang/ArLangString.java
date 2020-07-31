package com.stephenfox.lang;

public class ArLangString {
  private final String value;

  public ArLangString(String value) {
    // Remove quotes, as the string is parsed with the quotes from the lexer.
    this.value = value.substring(1, value.length() - 1);
  }

  public String value() {
    return value;
  }
}
