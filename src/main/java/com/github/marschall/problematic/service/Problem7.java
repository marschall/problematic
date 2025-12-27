package com.github.marschall.problematic.service;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.marschall.problematic.service.Problem7.ExchangePacket.ClientPacket;
import com.github.marschall.problematic.service.Problem7.ExchangePacket.ServerPacket;

final class Problem7 implements Problem {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final Exchanger<ExchangePacket> exchanger;

  private final Thread backgroundThread;

  Problem7() {
    this.exchanger = new Exchanger<>();
    this.backgroundThread = new Thread(this::spin, "message-queue-client");
    this.backgroundThread.setDaemon(true);
  }

  private void spin() {
    while (!Thread.currentThread().isInterrupted()) {
      var latch = new CountDownLatch(1);
      var packet = new ServerPacket(latch);
      try {
        ExchangePacket received = this.exchanger.exchange(packet);
        if (received instanceof ClientPacket clientPacket) {
          Object monitor = clientPacket.getMonitor();
          synchronized (monitor) {
            // signal other thread once lock is acquired
            latch.countDown();
            Thread.sleep(10L);
          }
        } else {
          LOG.warn("expected ClientPacket but got: " + received);
        }
      } catch (InterruptedException e) {
        LOG.trace("interrupted, existing", e);
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
    // waiting to acquire many synchronized
    try {
      // pass monitor between threads to prevent biasing
      var monitor = new Object();
      for (int i = 0; i < strength; i++) {
        var clientPacket = new ClientPacket(monitor);
        ExchangePacket received = this.exchanger.exchange(clientPacket);
        if (received instanceof ServerPacket serverPacket) {
          // ensure other thread already has lock
          serverPacket.getReady().await();
          synchronized (monitor) {
            LOG.debug("in monitor");
          }
        } else {
          LOG.warn("expected ServerPacket but got: " + received);
        }
      }
    } catch (InterruptedException  e) {
      LOG.debug("wait exchange: {}", e);
    }
    return "OK";
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_7;
  }

  static sealed class ExchangePacket {

    static final class ClientPacket extends ExchangePacket {

      private final Object monitor;

      ClientPacket(Object monitor) {
        this.monitor = Objects.requireNonNull(monitor);
      }

      Object getMonitor() {
        return this.monitor;
      }

    }

    static final class ServerPacket extends ExchangePacket {

      private final CountDownLatch ready;

      ServerPacket(CountDownLatch ready) {
        this.ready = Objects.requireNonNull(ready);
      }

      CountDownLatch getReady() {
        return this.ready;
      }

    }

  }

}
