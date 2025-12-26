package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MathUtilTests {

  @Test
  void fib() {
    assertEquals(0, MathUtil.fib(0));
    assertEquals(1, MathUtil.fib(1));
    assertEquals(1, MathUtil.fib(2));
    assertEquals(2, MathUtil.fib(3));
    assertEquals(3, MathUtil.fib(4));
    assertEquals(5, MathUtil.fib(5));
  }

}
