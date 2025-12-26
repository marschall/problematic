package com.github.marschall.problematic.service;

final class Problem12 implements Problem {

  @Override
  public Object withHighStrenght() {
    return withStrenght(5);
  }

  @Override
  public Object withLowStrenght() {
    return withStrenght(44);
  }

  private Object withStrenght(int strength) {
    if (strength < 0) {
      throw new IllegalArgumentException();
    }
    // recursion
    return Integer.toString(MathUtil.fib(strength));
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_12;
  }

}
