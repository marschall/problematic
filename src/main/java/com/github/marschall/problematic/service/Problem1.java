package com.github.marschall.problematic.service;

import java.util.Arrays;

final class Problem1 implements Problem {

  @Override
  public int getHighStrength() {
    return 1024 * 100;
  }

  @Override
  public int getLowStrength() {
    return 1024;
  }

  @Override
  public Object withStrength(int strength) {
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
