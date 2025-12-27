package com.github.marschall.problematic.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ProblemServiceTests {

  private ProblemService service;

  @BeforeEach
  void setUp() {
    this.service = new ProblemService();
    this.service.startUp();
  }

  @AfterEach
  void tearDown() {
    this.service.shutDown();
  }

  @ParameterizedTest
  @EnumSource(
      value = ProblemType.class,
      names = {"PROBLEM_1", "PROBLEM_2", "PROBLEM_3", "PROBLEM_4", "PROBLEM_5", "PROBLEM_6", "PROBLEM_7",
              "PROBLEM_9", "PROBLEM_10", "PROBLEM_11", "PROBLEM_12", "PROBLEM_13"})
  void withLowStrength(ProblemType problemType) {
    this.service.withLowStrength(problemType);
  }

}
