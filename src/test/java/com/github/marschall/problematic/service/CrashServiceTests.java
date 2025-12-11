package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrashServiceTests {
  
  private CrashService service;

  @BeforeEach
  void setUp() {
    this.service = new CrashService();
  }

  @Test
  void test() {
    assertThrows(OutOfMemoryError.class, this.service::crash1);
  }

}
