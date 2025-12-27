package com.github.marschall.problematic.service;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Problem6 implements Problem {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final CyclicBarrier barrier;

  private final Thread backgroundThread;
  
  Problem6() {
    this.barrier = new CyclicBarrier(2);
    this.backgroundThread = new Thread(this::spin, "background-worker");
    this.backgroundThread.setDaemon(true);
  }

  private void spin() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(TimeUnit.SECONDS.toMillis(5L));
        this.barrier.await();
      } catch (InterruptedException e) {
        LOG.trace("interrupted, existing", e);
        return;
      } catch (BrokenBarrierException e) {
        LOG.trace("broken barrier, existing", e);
        return;
      }
    }
  }

  @Override
  public void startUp() {
    this.backgroundThread.start();
  }

  @Override
  public void shutDown() {
    this.backgroundThread.interrupt();
  }

  @Override
  public int getHighStrength() {
    return 1_000;
  }

  @Override
  public int getLowStrength() {
    return 10;
  }

  @Override
  public Object withStrength(int strength) {
    // wait on lock
    long start = System.currentTimeMillis();
    try {
      while (System.currentTimeMillis() - start < strength) {
        long toWait = strength - (System.currentTimeMillis() - start);
        if (toWait <= 0) {
          break;
        }
        try {
          this.barrier.await(toWait, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
          LOG.trace("await timeout", e);
          break;
        }
      }
    } catch (InterruptedException | BrokenBarrierException e) {
      LOG.debug("await failed", e);
    }
    return "OK";
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_6;
  }

}
