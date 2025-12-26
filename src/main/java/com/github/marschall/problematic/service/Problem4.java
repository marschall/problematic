package com.github.marschall.problematic.service;

import java.lang.invoke.MethodHandles;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

final class Problem4 implements Problem {

  @Override
  public Object withHighStrenght() {
    return withStrenght(1024 * 1024);
  }

  @Override
  public Object withLowStrenght() {
    return withStrenght(10);
  }

  private Object withStrenght(int strength) {
    // logger lookup
    for (int i = 0; i < strength; i++) {
      Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass());
      if (logger.isDebugEnabled()) {
        logger.debug("at iteration: " + i);
      }
    }
    return "OK";
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_4;
  }

}
