package com.github.asciborek.concurrency;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class CompletableFutureTest {

  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

  @Test
  void anyOfReturnsFirstCompletedFuture() {
    CompletableFuture<String> firstCompletedFuture = CompletableFuture.supplyAsync(() -> {
      sleep(2);
      return "firstCompletedFuture";
    }, executor);
    CompletableFuture<Integer> secondCompletedFuture = CompletableFuture.supplyAsync(() -> {
      sleep(6);
      return -1;
    }, executor);
    CompletableFuture<Long> thirdCompletedFuture = CompletableFuture.supplyAsync(() -> {
      sleep(8);
      return 1L;
    }, executor);
    CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(secondCompletedFuture, thirdCompletedFuture, firstCompletedFuture);
    Awaitility.await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> Assertions.assertThat(anyOfFuture.get()).isEqualTo("firstCompletedFuture"));
  }

  @Test
  void anyOfReturnsExceptionallyIfFirstCompletedFutureThrowsException() {
    CompletableFuture<String> exceptionallyComplementedFuture = CompletableFuture.supplyAsync(() -> {
      sleep(1);
      throw new RuntimeException();
    }, executor);
    CompletableFuture<String> complementedFuture = CompletableFuture.supplyAsync(() -> {
      sleep(3);
      return "completedFuture";
    }, executor);
    CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(complementedFuture, exceptionallyComplementedFuture);

    Awaitility.await().atMost(Duration.ofSeconds(2)).untilAsserted(() -> Assertions.assertThatThrownBy(anyOfFuture::get).isInstanceOf(
        ExecutionException.class));
  }

  @Test
  void thenApplyIsNotCalledOnException() {
    AtomicBoolean thenAcceptWasCalled = new AtomicBoolean(false);
    CompletableFuture.<String>supplyAsync(() -> {
          sleep(2);
          return null;
        }, executor)
        .thenApply(String::length)
        .thenAccept(_ -> thenAcceptWasCalled.set(true));
    Awaitility.await()
        .pollDelay(Duration.ofSeconds(4))
        .untilAsserted(() -> Assertions.assertThat(thenAcceptWasCalled.get()).isFalse());
  }

  @Test
  void propagatesExceptionToHandleMethod() {
    CompletableFuture<Integer> future = CompletableFuture.<String>supplyAsync(() -> {
          sleep(5);
          return null;
        }, executor)
        .thenApply(String::length)
        .thenApply(length -> {
          sleep(10);
          return length * 2;
        }).handle((number, exception) -> {
          if (exception != null) {
            return 0;
          } else {
            return number;
          }
    });
    Awaitility.await().atMost(Duration.ofSeconds(6)).untilAsserted(() -> {
      Assertions.assertThat(future.get()).isEqualTo(0);
    });

  }

  private static void sleep(long seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @AfterAll
  void tearDownExecutor() {
    executor.shutdownNow();
  }
}
