package com.github.marschall.problematic.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

class CacheBeanTests {

  @Test
  void singleLeaf() {
    assertDoesNotThrow(() -> new CacheBean(1L));
  }

  @Test
  void twoLeaves() {
    assertDoesNotThrow(() -> new CacheBean(CacheBean.Leaf.LEAF_SIZE * 2L));
  }

  @Test
  void size() {
    ClassLayout rootLayout = ClassLayout.parseClass(CacheBean.Root.class);
    assertEquals(rootLayout.instanceSize(), CacheBean.Root.size());

    ClassLayout nodeLayout = ClassLayout.parseClass(CacheBean.Node.class);
    assertEquals(nodeLayout.instanceSize(), CacheBean.Node.size());

    ClassLayout leafLayout = ClassLayout.parseClass(CacheBean.Leaf.class);
    assertEquals(leafLayout.instanceSize(), CacheBean.Leaf.size());

    ClassLayout byteArrayLayout = ClassLayout.parseClass(byte[].class);
    assertEquals(byteArrayLayout.instanceSize(), CacheBean.Leaf.payloadSize());

    ClassLayout objectArrayLayout = ClassLayout.parseClass(Object[].class);
    assertEquals(objectArrayLayout.instanceSize(), CacheBean.Root.objectArraySize());

    ClassLayout arrayListLayout = ClassLayout.parseClass(ArrayList.class);
    assertEquals(arrayListLayout.instanceSize(), CacheBean.Root.arrayListSize());
  }

}
