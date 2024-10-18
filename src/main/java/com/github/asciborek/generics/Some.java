package com.github.asciborek.generics;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Some<T> implements Option<T> {

  private final T value;

  Some(T value) {
    Objects.requireNonNull(value, "value cannot be null");
    this.value = value;
  }

  @Override
  public boolean isPresent() {
    return true;
  }

  @Override
  public void ifPresent(Consumer<? super T> consumer) {
    consumer.accept(value);
  }

  @Override
  public Option<T> filter(Predicate<? super T> filter) {
    return filter.test(value) ? this : Option.empty();
  }

  @Override
  public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
    return new Some<>(mapper.apply(value));
  }

  public T get() {
    return value;
  }

  @Override
  public T orElse(T defaultValue) {
    return value;
  }

  @Override
  public T orElseGet(Supplier<? extends T> supplier) {
    return value;
  }

  @Override
  public  <X extends Throwable> T orElseThrow(Supplier<X> exception) {
    return value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Some<?> other)) {
      return false;
    }
    return Objects.equals(this.value, other.value);
  }


}
