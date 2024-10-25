package com.github.asciborek.stream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public final class StreamUtils {

  public static Optional<BigDecimal> average(Stream<BigDecimal> stream) {
    if (stream == null) {
      return Optional.empty();
    }
    return stream.reduce(BigDecimalAverager.SEED, BigDecimalAverager::apply, BigDecimalAverager::combine)
        .toAverage();
  }

  record BigDecimalAverager(BigDecimal sum, int count) {
    static BigDecimalAverager SEED = new BigDecimalAverager(BigDecimal.ZERO, 0);

    public BigDecimalAverager apply(BigDecimal bigDecimal) {
      return new BigDecimalAverager(sum.add(bigDecimal), count + 1);
    }

    public BigDecimalAverager combine(BigDecimalAverager another) {
      return new BigDecimalAverager(sum.add(another.sum()), count + another.count());
    }

    public Optional<BigDecimal> toAverage() {
      if (count == 0) {
        return Optional.empty();
      }
      BigDecimal average = sum.divide(BigDecimal.valueOf(count), RoundingMode.CEILING);
      return Optional.of(average);
    }

  }
}
