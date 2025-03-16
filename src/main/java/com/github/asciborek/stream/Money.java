package com.github.asciborek.stream;

import java.math.BigDecimal;

public record Money(BigDecimal amount, Currency currency)  {

  public static Money of(String amount, Currency currency) {
    return new Money(new BigDecimal(amount), currency);
  }

}
