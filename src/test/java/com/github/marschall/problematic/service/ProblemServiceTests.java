package com.github.marschall.problematic.service;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProblemServiceTests {

  private ProblemService service;

  @BeforeEach
  void setUp() {
    this.service = new ProblemService();
  }

  @Test
  void problem3() {
    this.service.problem3();
  }

  @Test
  void problem4() {
    this.service.problem4();
  }

  @Test
  void problem5() {
    this.service.problem5();
  }

  @Test
  void problem10() throws IOException {
    this.service.problem10();
  }

  @Test
  void problem11() throws IOException {
    this.service.problem11();
  }

}
