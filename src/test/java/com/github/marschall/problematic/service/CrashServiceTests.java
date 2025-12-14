package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.foreign.MemorySegment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrashServiceTests {

  private CrashService service;

  @BeforeEach
  void setUp() {
    this.service = new CrashService();
  }

  @Test
  void crash1() {
    assertThrows(OutOfMemoryError.class, this.service::crash1);
  }

  @Test
  void crash2() {
    assertThrows(OutOfMemoryError.class, this.service::crash2);
  }

  @Test
  void requestAndReleaseMemory() {
    MemorySegment segment = CrashService.requestMemory();
    CrashService.releaseMemory(segment);
  }

}
