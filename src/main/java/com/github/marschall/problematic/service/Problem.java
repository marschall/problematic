package com.github.marschall.problematic.service;

public interface Problem {

  Object withHighStrenght();

  Object withLowStrenght();

  ProblemType type();

}
