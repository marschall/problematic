package com.github.marschall.problematic.service;

final class Crash2 implements Crash {

  @Override
  public Object crash() {
    CacheBean cacheBean = new CacheBean();
    cacheBean.initializeRoot();
    return cacheBean;
  }

  @Override
  public CrashType type() {
    return CrashType.CRASH_2;
  }

}
