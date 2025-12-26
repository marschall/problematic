package com.github.marschall.problematic.service;

interface Problem {

  Object withHighStrenght();

  Object withLowStrenght();

  ProblemType type();

}
