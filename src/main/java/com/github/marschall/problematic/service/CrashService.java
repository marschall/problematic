package com.github.marschall.problematic.service;

import static com.github.marschall.problematic.ffi.Mman.MAP_ANON;
import static com.github.marschall.problematic.ffi.Mman.MAP_SHARED;
import static com.github.marschall.problematic.ffi.Mman.PROT_READ;
import static com.github.marschall.problematic.ffi.Mman.PROT_WRITE;
import static com.github.marschall.problematic.ffi.Mman.mmap;
import static com.github.marschall.problematic.ffi.Mman.munmap;
import static java.lang.foreign.MemorySegment.NULL;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.marschall.problematic.ffi.Unistd;

@Service
public class CrashService {

  public String crash1() {
    // out of heap memory
    List<ByteBuffer> buffers = new ArrayList<>();
    for (int i = 0; i < 1024; i++) {
      buffers.add(ByteBuffer.allocate(Integer.MAX_VALUE));
    }
    return buffers.toString();
  }

  public String crash2() {
    // out of off-heap memory
    List<ByteBuffer> buffers = new ArrayList<>();
    for (int i = 0; i < 1024; i++) {
      buffers.add(ByteBuffer.allocateDirect(Integer.MAX_VALUE));
    }
    return buffers.toString();
  }

  public String crash3() {
    // out of virtual memory
    List<MemorySegment> segments = new ArrayList<>();
    MemorySegment failed = MemorySegment.ofAddress(-1L);
    MemorySegment segment = requestMemory();
    while (!segment.equals(failed)) {
      segments.add(segment);
      segment = requestMemory();
    }
    // release last page to make enough headroom for JVM
    releaseMemory(segments.removeLast());
    touchPages(segments);
    return segments.toString();
  }

  private static void touchPages(List<MemorySegment> segments) {
    int pageSize = Unistd.getpagesize();
    for (MemorySegment each : segments) {
      // touch the pages
      for (long i = 0; i < each.byteSize(); i += pageSize) {
        each.set(ValueLayout.JAVA_BYTE, i, (byte) 1);
        // each.setAtIndex(ValueLayout.JAVA_BYTE, i, (byte) 1);
      }
    }
  }

  static void releaseMemory(MemorySegment segment) {
    munmap(segment, segment.byteSize());
  }

  static MemorySegment requestMemory() {
    // request multiple pages
    long len = (long) Unistd.getpagesize() * 1024L; // 4 MB on Linux, 16 MB on macOS
    int prot = PROT_READ() | PROT_WRITE();
    int flags = MAP_ANON() | MAP_SHARED();
    return mmap(NULL, len, prot, flags, -1, 0L).reinterpret(len);
  }

}
