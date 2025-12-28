package com.github.marschall.problematic.service;

/*
 * Large graph that fits into memory.
 * Small allocations outside that tip over the limit.
 */
final class Crash2 implements Crash {

  @Override
  public Object crash() {
    CacheBean cacheBean = new CacheBean();
    cacheBean.initializeRoot();
    return cacheBean;
  }

  static long getFreeMemory() {
    System.gc();
    return Runtime.getRuntime().freeMemory();
  }

  @Override
  public CrashType type() {
    return CrashType.CRASH_2;
  }

}
