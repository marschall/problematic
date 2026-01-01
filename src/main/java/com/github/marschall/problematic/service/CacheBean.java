package com.github.marschall.problematic.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CacheBean {

  private List<Root> roots;

  public CacheBean(long size) {
    this.roots = new ArrayList<>();
    CacheCreationContext context = new CacheCreationContext(size);
    while (context.hasMemoryLeft()) {
      roots.add(new Root(context));
      // account for size of reference in array list 
      context.decreaseByOneOop();
    }
  }

  /*
   * A full root is 1 GB.
   * 
   * root
   *  - 1024 nodes
   *    - 1024 leaves
   *      - 1024 B payload
   */
  static final class Root {

    static final int MAX_NODE_COUNT = 1024;

    static final int MAX_LEAF_COUNT = MAX_NODE_COUNT * Node.MAX_LEAF_COUNT;

    static int size() {
      return 16;
    }

    static int arrayListSize() {
      return 24;
    }

    static int objectArraySize() {
      return 16;
    }

    private final List<Node> nodes;

    Root(CacheCreationContext context) {
      this.nodes = new ArrayList<>();
      context.decreaseMemoryRequested(size());
      context.decreaseByOneArrayList();
      context.resetNodeCount();
      while (context.hasMemoryLeft() && context.getNodeCount() < MAX_NODE_COUNT) {
        this.nodes.add(new Node(context));
        context.incrementNodeCount();
        // account for size of reference in array list 
        context.decreaseByOneOop();
      }
    }

  }

  static final class Node {

    static final int MAX_LEAF_COUNT = 1024;

    static int size() {
      return 16;
    }

    private final List<Leaf> leaves;

    Node(CacheCreationContext context) {
      this.leaves = new ArrayList<>();
      context.resetLeafCount();
      context.decreaseMemoryRequested(size());
      context.decreaseByOneArrayList();
      while (context.hasMemoryLeft() && context.getNodeCount() < MAX_LEAF_COUNT) {
        this.leaves.add(new Leaf(context));
        context.incrementLeafCount();
        // account for size of reference in array list
        context.decreaseByOneOop();
      }
    }

  }

  static final class Leaf {

    static final int LEAF_SIZE = 1024;

    static int size() {
      return 16;
    }

    static int payloadSize() {
      return 16;
    }

    private byte[] payload;

    Leaf(CacheCreationContext context) {
      this.payload = new byte[LEAF_SIZE - size() - payloadSize()];
      context.decreaseMemoryRequested(LEAF_SIZE);
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(this.payload);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof Leaf other)) {
        return false;
      }
      return Arrays.equals(this.payload, other.payload);
    }

  }

  static final class CacheCreationContext {

    private long memoryRequested;

    private int leafCount;

    private int nodeCount;

    // wrong for ZGC
    static final int OOP_SIZE = 4;

    CacheCreationContext(long memoryRequested) {
      this.memoryRequested = memoryRequested;
      this.leafCount = 0;
      this.nodeCount = 0;
    }

    void decreaseMemoryRequested(long size) {
      this.memoryRequested -= size;
    }

    void decreaseByOneOop() {
      this.decreaseMemoryRequested(OOP_SIZE);
    }

    void decreaseByOneArrayList() {
      this.decreaseMemoryRequested(Root.arrayListSize());
      this.decreaseMemoryRequested(Root.objectArraySize());
    }

    boolean hasMemoryLeft() {
      return this.memoryRequested > 0L;
    }

    void incrementLeafCount() {
      this.leafCount += 1;
    }

    void resetLeafCount() {
      this.leafCount = 0;
    }

    int getLeafCount() {
      return this.leafCount;
    }

    void incrementNodeCount() {
      this.nodeCount += 1;
    }

    void resetNodeCount() {
      this.nodeCount = 0;
      this.leafCount = 0;
    }

    int getNodeCount() {
      return this.nodeCount;
    }
    
    @Override
    public String toString() {
      return this.getClass().getSimpleName() + '(' + this.memoryRequested + ')';
    }

  }

}
