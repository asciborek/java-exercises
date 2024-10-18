package com.github.asciborek.generics;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Option<T> permits Empty, Some {

  static <T> Option<T> empty() {
    @SuppressWarnings("unchecked")
    Option<T> EMPTY = (Option<T>) Empty.EMPTY_OPTION;
    return EMPTY;
  }

  static <T> Option<T> ofValue(final T value) {
    Objects.requireNonNull(value, "value cannot be null!");
    return new Some<>(value);
  }

  static <T> Option<T> ofNullable(final T value) {
    return value == null ? empty() : new Some<T>(value);
  }

  boolean isPresent();

  void ifPresent(Consumer<? super T> consumer);

  Option<T> filter(Predicate<? super T> filter);

  <U> Option<U> map(Function<? super T, ? extends U> mapper);

  T orElse(T defaultValue);

  T orElseGet(Supplier<? extends T> supplier);

  <X extends Throwable> T orElseThrow(Supplier<X> exception) throws X;

}
