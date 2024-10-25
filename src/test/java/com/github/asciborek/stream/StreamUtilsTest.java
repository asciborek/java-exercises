package com.github.asciborek.stream;

import com.github.asciborek.generics.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StreamUtilsTest {

  @MethodSource("averageBigDecimalData")
  @ParameterizedTest
  void countAverageBigDecimal(Stream<BigDecimal> stream, Optional<BigDecimal> expectedResult) {
    Optional<BigDecimal> average = StreamUtils.average(stream);
    Assertions.assertThat(average).isEqualTo(expectedResult);
  }

  static Stream<Arguments> averageBigDecimalData() {
    return Stream.of(
        Arguments.of(null, Optional.empty()),
        Arguments.of(Stream.of(), Optional.empty()),
        Arguments.of(Stream.of((BigDecimal.ONE)), Optional.of(BigDecimal.ONE)),
        Arguments.of(Stream.of(BigDecimal.valueOf(2), BigDecimal.valueOf(4)), Optional.of(BigDecimal.valueOf(3))),
        Arguments.of(Stream.of(new BigDecimal("3.50"), new BigDecimal("4.00"), new BigDecimal("7.50")), Optional.of(new BigDecimal("5.00")))
    );
  }

}
