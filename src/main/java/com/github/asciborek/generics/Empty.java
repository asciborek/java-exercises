package com.github.asciborek.generics;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Empty<T> implements Option<T> {

  @SuppressWarnings("rawtypes")
  static final Empty EMPTY_OPTION = new Empty<>();

  private Empty() {}

  @Override
  public boolean isPresent() {
    return false;
  }

  @Override
  public void ifPresent(Consumer<? super T> consumer) {
  }

  @Override
  public Option<T> filter(Predicate<? super T> filter) {
    return Option.empty();
  }

  @Override
  public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
    return Option.empty();
  }

  @Override
  public T orElse(T defaultValue) {
    return defaultValue;
  }

  @Override
  public T orElseGet(Supplier<? extends T> supplier) {
    return supplier.get();
  }

  @Override
  public <X extends Throwable> T orElseThrow(Supplier<X> exceptionSupplier) throws X {
    X exception = exceptionSupplier.get();
    throw exception;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Empty;
  }


}
