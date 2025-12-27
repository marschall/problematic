package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CrashServiceTests {

  private CrashService service;

  @BeforeEach
  void setUp() {
    this.service = new CrashService();
  }

  @ParameterizedTest
  @EnumSource(
      value = CrashType.class,
      names = {"CRASH_1", "CRASH_3"})
  void crash(CrashType crashType) {
    assertThrows(OutOfMemoryError.class, () -> this.service.crash(crashType));
  }

}
