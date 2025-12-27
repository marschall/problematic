package com.github.marschall.problematic.service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class ProblemService {

  private final Map<ProblemType, Problem> problems;

  ProblemService() {
    this.problems = new EnumMap<>(ProblemType.class);
    this.problems.put(ProblemType.PROBLEM_1, new Problem1());
    this.problems.put(ProblemType.PROBLEM_2, new Problem2());
    this.problems.put(ProblemType.PROBLEM_3, new Problem3());
    this.problems.put(ProblemType.PROBLEM_4, new Problem4());
    this.problems.put(ProblemType.PROBLEM_5, new Problem5());
    this.problems.put(ProblemType.PROBLEM_6, new Problem6());
    this.problems.put(ProblemType.PROBLEM_9, new Problem9());
    this.problems.put(ProblemType.PROBLEM_10, new Problem10());
    this.problems.put(ProblemType.PROBLEM_11, new Problem11());
    this.problems.put(ProblemType.PROBLEM_12, new Problem12());
    this.problems.put(ProblemType.PROBLEM_13, new Problem13());
  }

  @PostConstruct
  void startUp() {
    for (var problem : this.problems.values()) {
      problem.startUp();
    }
  }

  @PreDestroy
  void shutDown() {
    for (var problem : this.problems.values()) {
      problem.shutDown();
    }
  }
  
  public Object withLowStrength(ProblemType problemType) {
    Problem problem = this.problems.get(Objects.requireNonNull(problemType));
    if (problem == null) {
      throw new UnsupportedOperationException("Problem: " + problemType + " not supported");
    }
    return problem.withStrength(problem.getLowStrength());
  }

  public Object withHighStrength(ProblemType problemType) {
    Problem problem = this.problems.get(Objects.requireNonNull(problemType));
    if (problem == null) {
      throw new UnsupportedOperationException("Problem: " + problemType + " not supported");
    }
    return problem.withStrength(problem.getHighStrength());
  }

  public Object withHighStrengthOthersLow(ProblemType highStrengthType) {
    int result = 0;
    for (Problem problem : this.problems.values()) {
      int strength = problem.type() == highStrengthType ? problem.getHighStrength() : problem.getLowStrength();
      Object problemResult = problem.withStrength(strength);
      result += problemResult.hashCode();
    }
    return result;
  }

  public Object problem7() {
    // too many file descriptors
    return "OK";
  }

  public Object problem8() {
    // too many threads
    return "OK";
  }

}
