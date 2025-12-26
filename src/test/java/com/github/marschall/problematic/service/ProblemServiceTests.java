package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ProblemServiceTests {

  private ProblemService service;

  @BeforeEach
  void setUp() {
    this.service = new ProblemService();
  }

  @ParameterizedTest
  @EnumSource(value = ProblemType.class, names = {"PROBLEM_1", "PROBLEM_2", "PROBLEM_3", "PROBLEM_4", "PROBLEM_5"})
  void withLowStrength(ProblemType problemType) {
    this.service.withLowStrength(problemType);
  }

  @Test
  void problem9() {
    this.service.problem9();
  }

  @Test
  void problem10() throws IOException {
    this.service.problem10();
  }

  @Test
  void problem11() throws IOException {
    this.service.problem11();
  }

  @Test
  void problem12() throws IOException {
    this.service.problem12();
  }

  @Test
  void fib() {
    assertEquals(0, ProblemService.fib(0));
    assertEquals(1, ProblemService.fib(1));
    assertEquals(1, ProblemService.fib(2));
    assertEquals(2, ProblemService.fib(3));
    assertEquals(3, ProblemService.fib(4));
    assertEquals(5, ProblemService.fib(5));
  }

  @Test
  void problem13() throws IOException {
    this.service.problem13(1);
  }

}
