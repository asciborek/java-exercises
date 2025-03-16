package com.github.asciborek.stream;

import static com.github.asciborek.stream.Currency.PLN;
import static com.github.asciborek.stream.Currency.USD;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MoneyUtilsTest {


  static Stream<Arguments> maxByCurrencyArguments() {
    return Stream.of(
        Arguments.of(
            List.of(Money.of("10.0", PLN), Money.of("15.0", USD), Money.of("21.0", PLN)),
            Map.of(PLN, new BigDecimal("21.0"), USD, new BigDecimal("15.0"))
        ),
        Arguments.of(
            List.of(Money.of("10.0", PLN), Money.of("15.0", USD), Money.of("21.0", PLN), Money.of("20.0", PLN)),
            Map.of(PLN, new BigDecimal("21.0"), USD, new BigDecimal("15.0"))
        )
    );
  }

  @ParameterizedTest
  @MethodSource("maxByCurrencyArguments")
  void maxByCurrency(Collection<Money> monies, Map<Currency, BigDecimal> expectedMaxByCurrency) {
    Map<Currency, BigDecimal> maxByCurrency =  MoneyUtil.getMaxByCurrency(monies);
    Assertions.assertThat(maxByCurrency).isEqualTo(expectedMaxByCurrency);
  }

  static Stream<Arguments> sumAmountCurrencyArguments() {
    return Stream.of(
        Arguments.of(
            List.of(Money.of("10.0", PLN), Money.of("15.0", USD), Money.of("21.0", PLN)),
            Map.of(PLN, new BigDecimal("31.0"), USD, new BigDecimal("15.0"))
        ),
        Arguments.of(
            List.of(Money.of("10.0", PLN), Money.of("15.0", USD), Money.of("21.0", PLN), Money.of("20.0", PLN), Money.of("15.0", USD)),
            Map.of(PLN, new BigDecimal("51.0"), USD, new BigDecimal("30.0"))
        )
    );
  }

  @ParameterizedTest
  @MethodSource("sumAmountCurrencyArguments")
  void sumByCurrency(Collection<Money> monies, Map<Currency, BigDecimal> expectedSumByCurrency) {
    Map<Currency, BigDecimal> sumAmountByCurrency =  MoneyUtil.sumAmountByCurrency(monies);
    Assertions.assertThat(sumAmountByCurrency).isEqualTo(expectedSumByCurrency);
  }

}
