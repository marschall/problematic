package com.github.marschall.problematic.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

public class CacheBean {

  private Root root;

  @PostConstruct
  public void initializeRoot() {
    this.root = new Root();
    this.root.initializeNodes();
  }


  static final class Root {

    private final List<Node> nodes;

    Root() {
      this.nodes = new ArrayList<>();
    }

    void initializeNodes() {
      for (int i = 0; i < 1024; i++) {
        this.nodes.add(new Node());
      }
    }

  }

  static final class Node {

    private final List<Leaf> leaves;

    Node() {
      this.leaves = new ArrayList<>();
      for (int i = 0; i < 1024; i++) {
        this.leaves.add(new Leaf());
      }
    }

  }

  static final class Leaf {

    private byte[] payload;

    Leaf() {
      this.payload = new byte[1024];
    }

  }


}
