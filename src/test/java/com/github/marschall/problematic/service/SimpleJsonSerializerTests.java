package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class SimpleJsonSerializerTests {

  @Test
  void serialize() throws IOException {
    // preserve order
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("string", "va\"ue");
    map.put("number", 23);
    map.put("boolean", true);
    map.put("list", Collections.singletonList(null));
    StringWriter writer = new StringWriter();
    SimpleJsonSerializer.serializeMap(map, writer);
    assertEquals("{\"string\":\"va\\\"ue\",\"number\":23,\"boolean\":true,\"list\":[null]}", writer.toString());
  }

}
