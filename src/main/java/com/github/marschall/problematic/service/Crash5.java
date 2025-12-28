package com.github.marschall.problematic.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServer;

import com.github.marschall.problematic.ffi.Unistd;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;

/*
 * try to allocate max free direct memory
 * fail if -XX:MaxRAMPercentage > 50% and -XX:MaxDirectMemorySize hasn't been adjusted
 */
final class Crash5 implements Crash {
  
  /*
   * Do not allocate more than 8MB.
   * Large allocations 
   */
  private static final int MAX_ALLOCATION_SIZE = 8 * 1024 * 1024;
  
  private static final int ONE_MB = 1 * 1024 * 1024;

  private static final String DIRECT_BUFFER_POOL_NAME = "java.nio:type=BufferPool,name=direct";
  private final BufferPoolMXBean directBufferPool;
  private final HotSpotDiagnosticMXBean hotSpotDiagnostic;

  Crash5() {
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    try {
      this.directBufferPool = ManagementFactory.newPlatformMXBeanProxy(mbs, DIRECT_BUFFER_POOL_NAME, BufferPoolMXBean.class);
      this.hotSpotDiagnostic = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public Object crash() {
    System.gc();
    // make sure heap is expanded to maximum size
    // for cases where Xms < Xmx
    enlargeHeap();
    long maxDirectMemory = getMaxDirectMemory();
    long used = this.directBufferPool.getMemoryUsed();
    long available = maxDirectMemory - used;
    List<ByteBuffer> buffers = new ArrayList<>();
    while (available > MAX_ALLOCATION_SIZE) {
      buffers.add(ByteBuffer.allocateDirect(MAX_ALLOCATION_SIZE));
      available -= MAX_ALLOCATION_SIZE;
    }
    if (available > 0L) {
      if (available > 1024L) {
        // leave some room for VM
        ByteBuffer.allocateDirect(Math.toIntExact(available - 1024L));
      } else {
        buffers.add(ByteBuffer.allocateDirect(1));
      }
    }
    touchMemory(buffers);
    return "OK";
  }

  private long getMaxDirectMemory() {
    long maxDirectMemory = this.getVmOptionInBytes("MaxDirectMemorySize");
    if (maxDirectMemory > 0) {
      return maxDirectMemory;
    }
    // not set, in that case max heap.
    return this.getVmOptionInBytes("MaxHeapSize");
  }

  private long getVmOptionInBytes(String optionName) {
    VMOption option = this.hotSpotDiagnostic.getVMOption(optionName);
    return Long.parseLong(option.getValue());
  }

  private static void touchMemory(List<ByteBuffer> buffers) {
    for (ByteBuffer buffer : buffers) {
      touchMemory(buffer);
    }
  }

  private static void touchMemory(ByteBuffer buffer) {
    int pageSize = Unistd.getpagesize();
    // can overflow to negative
    for (int i = 0; i < buffer.capacity() && i > 0; i += pageSize) {
      buffer.put(i, (byte) 42);
    }

  }

  static void enlargeHeap() {
    long freeMemory = Runtime.getRuntime().freeMemory();
    allocateMemory(freeMemory);
  }
  
  static void allocateMemory(long bytes) {
    long remaining = bytes;
    List<byte[]> buffers = new ArrayList<>();
    while (remaining > 0) {
      int toAllocate;
      int toSubtract;
      if (remaining > ONE_MB) {
        toAllocate = remaining > ONE_MB ? ONE_MB - 16 : (int) remaining - 16;
        toSubtract = (int) ONE_MB;
      } else {
        toAllocate = (int) remaining - 16;
        toSubtract = (int) remaining;
      }
      if (toAllocate > 0) {
        buffers.add(new byte[toAllocate]);
        remaining -= toSubtract;
      } else {
        break;
      }
    }
  }

  @Override
  public CrashType type() {
    return CrashType.CRASH_5;
  }

}
