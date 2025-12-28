package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Crash5Tests {

  @Test
  void test() {
    Crash5 crash5 = new Crash5();
    assertNotNull(crash5.crash());
  }

}
