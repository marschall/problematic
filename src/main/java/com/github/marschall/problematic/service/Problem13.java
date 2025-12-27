package com.github.marschall.problematic.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

final class Problem13 extends AbstractProblem {

  @Override
  public int getHighStrength() {
    return 1_000_000;
  }

  @Override
  public int getLowStrength() {
    return 10;
  }

  @Override
  public Object withStrength(int strength) {
    // single byte reads
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
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return totalRead;
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_13;
  }

}
