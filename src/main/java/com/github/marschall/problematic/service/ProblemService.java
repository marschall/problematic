package com.github.marschall.problematic.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.classfile.ClassFile;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.constant.ClassDesc;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessFlag;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

  private final AtomicInteger classCounter;

  ProblemService() {
    this.barrier = new CyclicBarrier(2);
    this.backgroundThread = new Thread(this::spin, "background-thread");
    this.backgroundThread.setDaemon(true);
    this.classCounter = new AtomicInteger();
  }

  private void spin() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        Thread.sleep(TimeUnit.SECONDS.toMillis(5L));
        this.barrier.await();
      } catch (InterruptedException e) {
        LOG.info("interrupted, existing", e);
        return;
      } catch (BrokenBarrierException e) {
        LOG.info("broken barrier, existing", e);
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

  public Object problem1() {
    // 1 MB live set
    // -> allocation pressure
    byte[][] buffers = new byte[1024][];
    for (int i = 0; i < 1024 * 100; i++) {
      for (int j = 0; j < buffers.length; j++) {
        buffers[j] = new byte[1024];
      }
    }
    return buffers;
  }

  public Object problem2() {
    // allocate until we get OutOfMemoryError
    // remove from live set and add new
    // -> lots of GC churn due to high heap occupancy
    List<byte[]> buffers = new LinkedList<>();
    byte[] buffer = tryAllocate(1024);
    while (buffer != null) {
      buffers.add(buffer);
      buffer = tryAllocate(1024);
    }

    for (int i = 0; i < 1024 * 1024; i++) {
      buffers.remove(0);
      buffer = tryAllocate(1024);
      if (buffer != null) {
        buffers.add(buffer);
      }
    }

    return buffers;
  }

  private static byte[] tryAllocate(int size) {
    try {
      return new byte[size];
    } catch (OutOfMemoryError e) {
      return null;
    }
  }

  public Object problem3() {
    // excessive debug logging
    for (int i = 0; i < 1024 * 1024; i++) {
      LOG.debug("at iteration: " + i);
    }
    return "OK";
  }

  public Object problem4() {
    // logger lookup
    for (int i = 0; i < 1024 * 1024; i++) {
      Log logger = LogFactory.getLog(MethodHandles.lookup().lookupClass());
      if (logger.isDebugEnabled()) {
        LOG.debug("at iteration: {}", i);
      }
    }
    return "OK";
  }

  public Object problem5() {
    // regex matching
    return problem5(100);
  }
  
  public Object problem5(int strength) {
    if (strength < 0) {
      throw new IllegalArgumentException();
    }
    // regex matching
    String input;
    if (strength > 10) {
      input = "1".repeat(strength - 10) + "1234567890";
    } else {
      input = "1234567890".substring(0, strength);
    }
    return isNumeric(input + "1234567890.0") ? "true" : false;
  }

  private static boolean isNumeric(String s) {
    // try to trigger catastrophic backtracking, ReDoS
    return s.matches("(\\d|\\d\\d)+");
//    return s.matches("(\\d+)+[0-9]");
//    return s.matches("(\\d|\\d[0-9])+");
//    return s.matches("(\\d+)+");
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

  public Object problem9() throws IllegalAccessException {
    // too many classes
    var constantPoolBuilder = ConstantPoolBuilder.of();
    var classEntry = constantPoolBuilder.classEntry(
            ClassDesc.of(this.getClass().getPackageName(), "Generated" + this.classCounter.incrementAndGet()));
    var classFile = ClassFile.of();
    byte[] byteCode = classFile.build(classEntry, constantPoolBuilder, classBuilder -> {
      classBuilder.withFlags(AccessFlag.FINAL);
    });
    return MethodHandles.lookup().defineClass(byteCode);
  }
  
  public Object problem10() throws IOException {
    return problem10(10_000);
  }

  public Object problem10(int strength) throws IOException {
    // OuputStreamWriter
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Map<String, Object> map = new HashMap<>();
    List<String> value = Collections.nCopies(1_000, "json");
    for (int i = 0; i < strength; i++) {
      map.put(Integer.toString(i), value);
    }
    try (var writer = new OutputStreamWriter(bos, UTF_8)) {
      SimpleJsonSerializer.serializeMap(map, writer);
    }
    return bos.size();
  }

  public Object problem11() throws IOException {
    // Too many exceptions
    boolean isSymlink = true;
    Path p = Path.of("/typo");
    for (int i = 0; i < 1024; i++) {
      isSymlink &= Files.isSymbolicLink(p);
    }
    return isSymlink ? "failed" : "OK";
  }

  public Object problem12() {
    return problem12(44);
  }

  public Object problem12(int strength) {
    if (strength < 0) {
      throw new IllegalArgumentException();
    }
    // recursion
    return Integer.toString(fib(strength));
  }

  static int fib(int i) {
    if (i <= 1) {
      return i;
    }
    return fib(i - 1) + fib(i - 2);
  }

  public Object problem13() throws IOException {
    return problem13(1_000_000);
  }

  Object problem13(int strength) throws IOException {
    long totalRead = 0L;
    try (var arena = Arena.ofConfined();
         var fileChannel = FileChannel.open(Path.of("/dev/random"), StandardOpenOption.READ)) {
      long bufferSize = 1L;
      MemorySegment memorySegment = arena.allocate(bufferSize);
      ByteBuffer byteBuffer = memorySegment.asByteBuffer();
      for (int i = 0; i < strength; i++) {
        byteBuffer.clear();
        int read = fileChannel.read(byteBuffer);
        if (read != bufferSize) {
          throw new IllegalStateException();
        }
        totalRead += read;
      }
    }
    return totalRead;
  }

}
