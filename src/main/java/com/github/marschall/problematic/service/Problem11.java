package com.github.marschall.problematic.service;

import java.nio.file.Files;
import java.nio.file.Path;

final class Problem11 implements Problem {

  @Override
  public Object withHighStrenght() {
    return withStrenght(10_000);
  }

  @Override
  public Object withLowStrenght() {
    return withStrenght(10);
  }

  private Object withStrenght(int strength) {
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
