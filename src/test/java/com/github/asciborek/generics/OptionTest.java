package com.github.asciborek.generics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OptionTest {

  @Test
  void emptyCreatesEmptyOption() {
    Option<String> emptyOption = Option.empty();
    assertThat(emptyOption).isExactlyInstanceOf(Empty.class);
    assertThat(emptyOption.isPresent()).isFalse();
  }

  @Test
  void valueOfThrowsExceptionIfValueIsNull() {
    assertThatThrownBy(() -> Option.ofValue(null)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void valueOfCreatesSomeOptionIfValueIsNotNull() {
    BigDecimal value = BigDecimal.ONE;
    Option<BigDecimal> option = Option.ofValue(value);
    Some<BigDecimal> some = (Some<BigDecimal>) option;

    assertThat(option).isExactlyInstanceOf(Some.class);
    assertThat(option.isPresent()).isTrue();
    assertThat(some.get()).isEqualTo(value);
  }

  @Test
  void ofNullableReturnsEmptyOptionIfValueIsNull() {
    Option<String> option = Option.ofNullable(null);
    assertThat(option).isExactlyInstanceOf(Empty.class);
    assertThat(option.isPresent()).isFalse();
  }

  @Test
  void ofNullableReturnsSomeOptionIfValueIsNotNull() {
    BigDecimal value = BigDecimal.ONE;
    Option<BigDecimal> option = Option.ofNullable(value);
    Some<BigDecimal> some = (Some<BigDecimal>) option;

    assertThat(option).isExactlyInstanceOf(Some.class);
    assertThat(option.isPresent()).isTrue();
    assertThat(some.get()).isEqualTo(value);
  }

  @Test
  void ifPresentIsNotRunByEmptyOption() {
    AtomicReference<String> value = new AtomicReference<>(null);
    Option<String> emptyOption = Option.empty();
    emptyOption.ifPresent(value::set);
    assertThat(value.get()).isNull();
  }

  @Test
  void ifPresentIsRunBySomeOption() {
    String optionValue = "value";
    AtomicReference<String> value = new AtomicReference<>(null);
    Option<String> emptyOption = Option.ofValue(optionValue);
    emptyOption.ifPresent(value::set);
    assertThat(value.get()).isEqualTo(optionValue);
  }

  @Test
  void filterOnEmptyOptionReturnsEmptyOption() {
    Option<String> filteredOption = Option.<String>empty().filter(Predicate.not(String::isEmpty));
    assertThat(filteredOption).isExactlyInstanceOf(Empty.class);
    assertThat(filteredOption.isPresent()).isFalse();
  }

  @Test
  void filterReturnsSomeOptionIfValueMatchesPredicate() {
    String optionValue = "value";
    Option<String> originalOption = Option.ofValue(optionValue);
    Some<String> filtered = (Some<String>)(originalOption.filter(Predicate.not(String::isBlank)));
    assertThat(filtered.get()).isEqualTo(optionValue);
  }

  @Test
  void filterReturnsEmptyOptionIfValueDoesNotMatchPredicate() {
    String optionValue = "";
    Option<String> originalOption = Option.ofValue(optionValue);
    Option<String> filtered = originalOption.filter(Predicate.not(String::isBlank));
    assertThat(filtered).isExactlyInstanceOf(Empty.class);
    assertThat(filtered.isPresent()).isFalse();
  }

  @Test
  void mapEmptyReturnEmpty() {
    Option<String> originalOption = Option.empty();
    Option<Integer> mappedOption = originalOption.map(String::length);
    assertThat(mappedOption.isPresent()).isFalse();
    assertThat(mappedOption).isExactlyInstanceOf(Empty.class);
  }

  @Test
  void mapExistingValueReturnSomeOption() {
    Some<Integer> mappedOption = (Some<Integer>)(Option.ofValue("test").map(String::length));
    assertThat(mappedOption.isPresent()).isTrue();
    assertThat(mappedOption.get()).isEqualTo(4);
  }

  @Test
  void orElseReturnDefaultValueForEmptyOption() {
    String defaultValue = "";
    assertThat(Option.empty().orElse(defaultValue)).isEqualTo(defaultValue);
  }

  @Test
  void orElseReturnAlreadyExistingValueForSomeOption() {
    String value = "Java";
    String defaultValue = "Scala";
    assertThat(Option.ofNullable(value).orElse(defaultValue)).isEqualTo(value);
  }

  @Test
  void orElseGetSupplyDefaultValueForEmptyOption() {
    String defaultValue = "suppliedValue";
    String value = Option.<String>empty().orElseGet(() -> defaultValue);
    assertThat(value).isEqualTo(defaultValue);
  }

  @Test
  void orElseGetReturnsCurrentValueForSomeOption() {
    String expectedValue = "test value";
    String value = Option.ofNullable(expectedValue).orElseGet(() -> "Supplier value");
    assertThat(value).isEqualTo(expectedValue);
  }

  @Test
  void orElseThrowThrowsExceptionOnEmptyOption() {
    final Option<String> empty = Option.empty();
    assertThatThrownBy(() -> empty.orElseThrow(() -> new RuntimeException("Empty Option!")))
        .isExactlyInstanceOf(RuntimeException.class);
  }

  @ParameterizedTest
  @MethodSource("emptyEqualsTestParameters")
  void emptyEqualsOnlyOtherEmpty(Option<String> option, Object other, boolean expected) {
    assertThat(option.equals(other)).isEqualTo(expected);
  }

  static Stream<Arguments> emptyEqualsTestParameters() {
    return Stream.of(
        Arguments.of(Option.empty(), Option.empty(), true),
        Arguments.of(Option.empty(), Option.ofValue("some"), false),
        Arguments.of(Option.empty(), null, false));
  }

  @ParameterizedTest
  @MethodSource("someEqualsTestParameters")
  void someEqualsOtherOnlyIfOtherIsSomeWithTheSameValue(Option<String> option, Object other, boolean expected) {
    assertThat(option.equals(other)).isEqualTo(expected);
  }

  static Stream<Arguments> someEqualsTestParameters() {
    return Stream.of(
        Arguments.of(Option.ofValue("Java"), Option.ofValue("Java"), true),
        Arguments.of(Option.ofValue("Java"), "Java", false),
        Arguments.of(Option.ofValue("Java"), Option.ofValue("Scala"), false),
        Arguments.of(Option.ofValue("Java"), Option.empty(), false),
        Arguments.of(Option.empty(), null, false));
  }


}
