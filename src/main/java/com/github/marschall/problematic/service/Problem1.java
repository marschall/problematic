package com.github.marschall.problematic.service;

import java.util.Arrays;

public final class Problem1 implements Problem {

  @Override
  public Object withHighStrenght() {
    return withStrenght(1024 * 100);
  }

  @Override
  public Object withLowStrenght() {
    return withStrenght(1024);
  }

  private Object withStrenght(int strength) {
    // 1 MB live set
    // -> allocation pressure
    byte[][] buffers = new byte[1024][];
    for (int i = 0; i < strength; i++) {
      for (int j = 0; j < buffers.length; j++) {
        buffers[j] = new byte[1024];
      }
    }
    return Arrays.hashCode(buffers);
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_1;
  }

}
