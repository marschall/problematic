package com.github.marschall.problematic.service;

final class MathUtil {

  private MathUtil() {
    throw new AssertionError("not instantiable");
  }

  static int fib(int i) {
    if (i <= 1) {
      return i;
    }
    return fib(i - 1) + fib(i - 2);
  }

}
