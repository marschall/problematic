package com.github.marschall.problematic.service;

final class Problem12 implements Problem {

  @Override
  public int getHighStrength() {
    return 44;
  }

  @Override
  public int getLowStrength() {
    return 5;
  }

  @Override
  public Object withStrength(int strength) {
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
