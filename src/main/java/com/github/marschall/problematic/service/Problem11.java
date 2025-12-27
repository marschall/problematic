package com.github.marschall.problematic.service;

import java.nio.file.Files;
import java.nio.file.Path;

final class Problem11 extends AbstractProblem {

  @Override
  public int getHighStrength() {
    return 10_000;
  }

  @Override
  public int getLowStrength() {
    return 10;
  }

  @Override
  public Object withStrength(int strength) {
    // Too many exceptions
    boolean isSymlink = true;
    var path = Path.of("/typo");
    for (int i = 0; i < strength; i++) {
      isSymlink &= Files.isSymbolicLink(path);
    }
    return isSymlink ? "failed" : "OK";
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_11;
  }

}
