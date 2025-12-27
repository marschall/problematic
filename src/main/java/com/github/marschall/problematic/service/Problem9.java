package com.github.marschall.problematic.service;

import java.lang.classfile.ClassFile;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.constant.ClassDesc;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessFlag;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

final class Problem9 extends AbstractProblem {

  private final AtomicInteger classCounter;

  Problem9() {
    this.classCounter = new AtomicInteger();
  }

  @Override
  public int getHighStrength() {
    return 1000;
  }

  @Override
  public int getLowStrength() {
    return 10;
  }

  @Override
  public Object withStrength(int strength) {
    // too many classes
    var constantPoolBuilder = ConstantPoolBuilder.of();
    List<Class<?>> classes = new ArrayList<>(strength);
    var lookup = MethodHandles.lookup();
    for (int i = 0; i < strength; i++) {
      var classEntry = constantPoolBuilder.classEntry(
          ClassDesc.of(this.getClass().getPackageName(), "Generated" + this.classCounter.incrementAndGet()));
      var classFile = ClassFile.of();
      byte[] byteCode = classFile.build(classEntry, constantPoolBuilder, classBuilder -> {
        classBuilder.withFlags(AccessFlag.FINAL);
      });
      try {
        classes.add(lookup.defineClass(byteCode));
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
    return classes.hashCode();
  }

  @Override
  public ProblemType type() {
    return ProblemType.PROBLEM_9;
  }

}
