package com.github.marschall.problematic.service;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Problem3 extends AbstractProblem {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public int getHighStrength() {
    return 1024 * 1024;
  }

  @Override
  public int getLowStrength() {
    return 10;
  }

  @Override
  public Object withStrength(int strength) {
    // excessive debug logging
    for (int i = 0; i < strength; i++) {
      LOG.debug("at iteration: " + i);
    }
    return "OK";
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_3;
  }

}
