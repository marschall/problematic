package com.github.marschall.problematic.service;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Problem10 implements Problem {

  @Override
  public Object withHighStrenght() {
    return withStrenght(10_000);
  }

  @Override
  public Object withLowStrenght() {
    return withStrenght(10);
  }

  private Object withStrenght(int strength) {
 // OuputStreamWriter
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Map<String, Object> map = new HashMap<>();
    List<String> value = Collections.nCopies(1_000, "json");
    for (int i = 0; i < strength; i++) {
      map.put(Integer.toString(i), value);
    }
    try (var writer = new OutputStreamWriter(bos, UTF_8)) {
        SimpleJsonSerializer.serializeMap(map, writer);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return bos.size();
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_10;
  }

}
