package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class CrashServiceTests {

  private CrashService service;

  @Autowired
  private ApplicationContext applicationContext;

  @BeforeEach
  void setUp() {
    this.service = new CrashService(this.applicationContext);
  }

  @ParameterizedTest
  @EnumSource(
      value = CrashType.class,
      names = {"CRASH_1", "CRASH_3"})
  void crash(CrashType crashType) {
    assertThrows(OutOfMemoryError.class, () -> this.service.crash(crashType));
  }

}
