package com.stephenfox.lang;

// Internally represent all numbers as integers.
public class ArLangNumber implements ArLangConstruct {
  int value;

  public ArLangNumber(String value) {
    this.value = Integer.parseInt(value);
  }

  public int value() {
    return value;
  }
}
