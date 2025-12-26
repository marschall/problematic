package com.github.marschall.problematic.service;

import java.util.LinkedList;
import java.util.List;

public final class Problem2 implements Problem {

  @Override
  public Object withHighStrenght() {
    return withStrenght(1024 * 1024);
  }

  @Override
  public Object withLowStrenght() {
    // suppress OutOfMemoryError
    return new Object();
  }

  private Object withStrenght(int strength) {
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
