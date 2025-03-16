package com.github.asciborek.stream;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public final class MoneyUtil {
  private MoneyUtil() {}

  public static Map<Currency, BigDecimal> getMaxByCurrency(Collection<Money> monies) {
   return monies.stream()
       .collect(Collectors.toMap(Money::currency, Money::amount, BinaryOperator.maxBy(BigDecimal::compareTo)));
  }

  public static Map<Currency, BigDecimal> sumAmountByCurrency(Collection<Money> monies) {
    return monies.stream()
        .collect(Collectors.toMap(Money::currency, Money::amount, BigDecimal::add));
  }

}
