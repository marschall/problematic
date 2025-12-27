package com.github.marschall.problematic.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

final class Crash1 implements Crash {

  @Override
  public Object crash() {
    // out of heap memory
    List<ByteBuffer> buffers = new ArrayList<>();
    for (int i = 0; i < 1024; i++) {
      buffers.add(ByteBuffer.allocate(Integer.MAX_VALUE));
    }
    return buffers.toString();
  }

  @Override
  public CrashType type() {
    return CrashType.CRASH_1;
  }

}
