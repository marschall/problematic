package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.foreign.MemorySegment;

import org.junit.jupiter.api.Test;

class Crash4Tests {

  @Test
  void requestAndReleaseMemory() {
    MemorySegment segment = Crash4.requestMemory();
    Crash4.releaseMemory(segment);
  }


}
