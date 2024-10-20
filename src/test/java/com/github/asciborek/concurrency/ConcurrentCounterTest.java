package com.github.asciborek.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ConcurrentCounterTest {

  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

  @Test
  void incrementCounter() {
    ConcurrentCounter counter = new ConcurrentCounter();
    assertThat(counter.getValue()).isEqualTo(0);
    assertThat(counter.incrementAndGet()).isEqualTo(1);
    assertThat(counter.getValue()).isEqualTo(1);
  }

  @Test
  void concurrentIncrementCounterWithDefaultSummandValue() {
    ConcurrentCounter counter = new ConcurrentCounter();
    for (int i = 0; i < 10000; i++) {
      executor.execute(counter::incrementAndGet);
    }
    Awaitility.await().pollDelay(Duration.ofMillis(100))
        .untilAsserted(() -> assertThat(counter.getValue()).isEqualTo(10000));
  }

  @Test
  void concurrentIncrementCounterWithCustomSummandValue() {
    ConcurrentCounter counter = new ConcurrentCounter();
    for (int i = 0; i < 10000; i++) {
      executor.execute(() -> counter.incrementAndGet(5));
    }
    Awaitility.await().pollDelay(Duration.ofMillis(100))
        .untilAsserted(() -> assertThat(counter.getValue()).isEqualTo(50000));
  }

  @AfterAll
  void shutdownExecutor() {
    executor.shutdownNow();
  }

}
