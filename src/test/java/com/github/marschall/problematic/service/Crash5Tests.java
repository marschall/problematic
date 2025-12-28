package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Crash5Tests {
  
  private Crash crash;

  @BeforeEach
  void setUp() {
    crash = new Crash5();
  }

  @Test
  void crash() {
    assertNotNull(crash.crash());
  }

}
