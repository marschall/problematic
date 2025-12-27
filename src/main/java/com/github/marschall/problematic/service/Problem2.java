package com.github.marschall.problematic.service;

import java.util.LinkedList;
import java.util.List;

final class Problem2 extends AbstractProblem {
  
  private static final int SKIP = 1;

  @Override
  public int getHighStrength() {
    return 1024 * 1024;
  }

  @Override
  public int getLowStrength() {
    // suppress OutOfMemoryError
    return SKIP;
  }

  @Override
  public Object withStrength(int strength) {
    if (strength == SKIP) {
      // suppress OutOfMemoryError
      return new Object();
    }
    // allocate until we get OutOfMemoryError
    // remove from live set and add new
    // -> lots of GC churn due to high heap occupancy
    List<byte[]> buffers = new LinkedList<>();
    byte[] buffer = tryAllocate(1024);
    while (buffer != null) {
      buffers.add(buffer);
      buffer = tryAllocate(1024);
    }

    for (int i = 0; i < strength; i++) {
      buffers.remove(0);
      buffer = tryAllocate(1024);
      if (buffer != null) {
        buffers.add(buffer);
      }
    }

    return buffers.hashCode();
  }

  private static byte[] tryAllocate(int size) {
    try {
      return new byte[size];
    } catch (OutOfMemoryError e) {
      return null;
    }
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_2;
  }

}
