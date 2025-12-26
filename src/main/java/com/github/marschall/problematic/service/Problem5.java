package com.github.marschall.problematic.service;

public final class Problem5 implements Problem {

  @Override
  public Object withHighStrenght() {
    return withStrenght(100);
  }

  @Override
  public Object withLowStrenght() {
    return withStrenght(10);
  }

  private Object withStrenght(int strength) {

    if (strength < 0) {
      throw new IllegalArgumentException();
    }
    // regex matching
    String input;
    if (strength > 10) {
      input = "1".repeat(strength - 10) + "1234567890";
    } else {
      input = "1234567890".substring(0, strength);
    }
    return isNumeric(input + "1234567890.0") ? "true" : false;
  }

  private static boolean isNumeric(String s) {
    // try to trigger catastrophic backtracking, ReDoS
    return s.matches("(\\d|\\d\\d)+");
//    return s.matches("(\\d+)+[0-9]");
//    return s.matches("(\\d|\\d[0-9])+");
//    return s.matches("(\\d+)+");
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_5;
  }

}
