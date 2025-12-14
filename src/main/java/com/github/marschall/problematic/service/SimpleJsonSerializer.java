package com.github.marschall.problematic.service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

final class SimpleJsonSerializer {

  static void serializeMap(Map<String, ?> m, Writer writer) throws IOException {
    writer.write('{');
    boolean first = true;
    for (Map.Entry<String, ?> entry : m.entrySet()) {
      if (!first) {
        writer.write(',');
      }
      first = false;
      String key = entry.getKey();
      serializeString(key, writer);
      writer.write(':');
      Object value = entry.getValue();
      serializeObject(value, writer);
    }
    writer.write('}');
  }
  
  static void serializeList(List<?> l, Writer writer) throws IOException {
    writer.write('[');
    boolean first = true;
    for (Object value : l) {
      if (!first) {
        writer.write(',');
      }
      first = false;
      serializeObject(value, writer);
    }
    writer.write(']');
  }
  
  static void serializeObject(Object o, Writer writer) throws IOException {
    switch (o) {
      case Number n -> serializeNumber(n, writer);
      case String s -> serializeString(s, writer);
      case Boolean b -> serializeBoolean(b, writer);
      case Map m -> serializeMap(m, writer);
      case List<?> l -> serializeList(l, writer);
      case null ->  serializeNull(writer);
      default -> throw new IllegalArgumentException("unknown type: " + o.getClass());
    }
  }

  static void serializeString(String s, Writer writer) throws IOException {
    writer.write('"');
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '"' -> 
          writer.write("\\\"");
        case '\\' -> 
          writer.write("\\\\");
        case '/' -> 
        writer.write("\\/");
        case '\b' -> 
          writer.write("\\b");
        case '\f' -> 
          writer.write("\\f");
        case '\n' -> 
          writer.write("\\n");
        case '\r' -> 
          writer.write("\\r");
        case '\t' -> 
          writer.write("\\t");
        default ->
          writer.write(c);
      }
    }
    writer.write('"');
  }

  static void serializeNumber(Number n, Writer writer) throws IOException {
    writer.write(n.toString());
  }
  
  static void serializeBoolean(boolean b, Writer writer) throws IOException {
    if (b) {
      writer.write("true");
    } else {
      writer.write("false");
    }
  }
  
  static void serializeNull(Writer writer) throws IOException {
    writer.write("null");
  }

}
