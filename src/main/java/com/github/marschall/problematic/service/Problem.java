package com.github.marschall.problematic.service;

interface Problem {

  int getHighStrength();
  
  int getLowStrength();

  Object withStrength(int strength);

  ProblemType type();

}
