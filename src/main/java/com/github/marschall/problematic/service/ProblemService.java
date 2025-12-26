package com.github.marschall.problematic.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class ProblemService {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final CyclicBarrier barrier;

  private final Thread backgroundThread;

  private final Map<ProblemType, Problem> problems;

  ProblemService() {
    this.barrier = new CyclicBarrier(2);
    this.backgroundThread = new Thread(this::spin, "background-thread");
    this.backgroundThread.setDaemon(true);
    this.problems = new EnumMap<>(ProblemType.class);
    this.problems.put(ProblemType.PROBLEM_1, new Problem1());
    this.problems.put(ProblemType.PROBLEM_2, new Problem2());
    this.problems.put(ProblemType.PROBLEM_3, new Problem3());
    this.problems.put(ProblemType.PROBLEM_4, new Problem4());
    this.problems.put(ProblemType.PROBLEM_5, new Problem5());
    this.problems.put(ProblemType.PROBLEM_9, new Problem9());
    this.problems.put(ProblemType.PROBLEM_10, new Problem10());
    this.problems.put(ProblemType.PROBLEM_11, new Problem11());
    this.problems.put(ProblemType.PROBLEM_12, new Problem12());
    this.problems.put(ProblemType.PROBLEM_13, new Problem13());
  }

  private void spin() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(TimeUnit.SECONDS.toMillis(5L));
        this.barrier.await();
      } catch (InterruptedException e) {
        LOG.debug("interrupted, existing", e);
        return;
      } catch (BrokenBarrierException e) {
        LOG.debug("broken barrier, existing", e);
        return;
      }
    }
  }

  @PostConstruct
  void startBackgroundThread() {
    this.backgroundThread.start();
  }

  @PreDestroy
  void stopBackgroundThread() {
    this.backgroundThread.interrupt();
  }
  
  public Object withLowStrength(ProblemType problemType) {
    Problem problem = this.problems.get(Objects.requireNonNull(problemType));
    if (problem == null) {
      throw new UnsupportedOperationException("Problem: " + problemType + " not supported");
    }
    return problem.withLowStrenght();
  }

  public Object withHighStrength(ProblemType problemType) {
    Problem problem = this.problems.get(Objects.requireNonNull(problemType));
    if (problem == null) {
      throw new UnsupportedOperationException("Problem: " + problemType + " not supported");
    }
    return problem.withHighStrenght();
  }

  public Object withHighStrengthOthersLow(ProblemType highStrengthType) {
    int result = 0;
    for (Problem problem : this.problems.values()) {
      Object problemResult = problem.type() == highStrengthType ? problem.withHighStrenght() : problem.withLowStrenght();
      result += problemResult.hashCode();
    }
    return result;
  }

  public Object problem6() {
    // wait on lock
    try {
      this.barrier.await(10L, TimeUnit.SECONDS);
    } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
      LOG.debug("wait failed: {}", e);
    }
    return "OK";
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
