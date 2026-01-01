package com.github.marschall.problematic.service;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CrashService {

  private final Map<CrashType, Crash> crashes;

  CrashService(ApplicationContext applicationContext) {
    this.crashes = new EnumMap<>(CrashType.class);
    this.crashes.put(CrashType.CRASH_1, new Crash1());
    this.crashes.put(CrashType.CRASH_2, new Crash2(applicationContext));
    this.crashes.put(CrashType.CRASH_3, new Crash3());
    this.crashes.put(CrashType.CRASH_4, new Crash4());
    this.crashes.put(CrashType.CRASH_5, new Crash5());
  }

  public Object crash(CrashType crashType) {
    return this.crashes.get(crashType).crash();
  }

}
