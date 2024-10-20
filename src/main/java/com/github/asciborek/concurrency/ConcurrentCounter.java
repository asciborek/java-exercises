package com.github.asciborek.concurrency;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ConcurrentCounter {

  private int counter;
  private final ReadLock readLock;
  private final WriteLock writeLock;

  public ConcurrentCounter() {
   this(0);
  }

  public ConcurrentCounter(int counter) {
    if (counter < 0) {
      throw new IllegalArgumentException("counter must be a natural number");
    }
    this.counter = counter;
    var lock = new ReentrantReadWriteLock();
    this.readLock = lock.readLock();
    this.writeLock = lock.writeLock();
  }

  public int incrementAndGet() {
    try {
      writeLock.lock();
      return incrementAndGet(1);
    } finally {
      writeLock.unlock();
    }
  }

  public int incrementAndGet(int summand) {
    try {
      writeLock.lock();
      this.counter += summand;
    } finally {
      writeLock.unlock();
    }
    return counter;
  }

  public int getValue() {
    int value;
    try {
      readLock.lock();
      value = counter;
    } finally {
      readLock.unlock();
    }
    return value;
  }

}
