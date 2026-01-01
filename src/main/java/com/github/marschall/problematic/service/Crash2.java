package com.github.marschall.problematic.service;

import java.lang.invoke.MethodHandles;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/*
 * Large graph that fits into memory.
 * Small allocations outside that tip over the limit.
 */
final class Crash2 implements Crash {
  
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private ApplicationContext applicationContext;

  public Crash2(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public Object crash() {
    long freeMemory = getFreeMemory();
    long headRoom = (long) (freeMemory * 0.2d);
    preloadCache(freeMemory - headRoom);
    String logMessage;
    try {
      logMessage = buildLogMessage(Math.toIntExact(headRoom));
    } finally {
      this.resetCache();
    }
    LOG.debug(logMessage);
    return logMessage;
  }
  
  private void preloadCache(long size) {
    CacheBean cacheBean = new CacheBean(size);
    // hide it away from stack
    applicationContext.getBean(CacheHolder.class).setCache(cacheBean);
  }
  
  private void resetCache() {
    applicationContext.getBean(CacheHolder.class).setCache(null);
  }

  private static String buildLogMessage(int headRoom) {
    StringBuilder buffer = new StringBuilder();
    buffer.append("values: ");
    buffer.append('[');
    for (int i = 0; i < headRoom; i++) {
      if (i > 0) {
        buffer.append(", ");
      }
      buffer.append(i);
    }
    buffer.append(']');
    return buffer.toString();
  }

  static long getFreeMemory() {
    MemoryMXBean memBean = ManagementFactory.getMemoryMXBean() ;
    MemoryUsage heapMemoryUsage = memBean.getHeapMemoryUsage();
    System.gc();
    return heapMemoryUsage.getMax() - heapMemoryUsage.getUsed();
  }

  @Override
  public CrashType type() {
    return CrashType.CRASH_2;
  }

}
