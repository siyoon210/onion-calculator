package me.siyoon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringCalculatorTest {
    private StringCalculator stringCalculator;

    @BeforeEach
    public void setUp() {
        stringCalculator = new StringCalculator();
    }

    @Test
    @DisplayName("기본구분자로 나누어져 있는 숫자들의 합을 계산한다.")
    public void basicCalcSum() {
        assertThat(stringCalculator.sum("1,2"), is(3));
        assertThat(stringCalculator.sum("1,2:3"), is(6));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("빈 문자열이나 null이 전달된 경우 0을 반환한다.")
    public void emptyOrNull_return0(String str) {
        assertThat(stringCalculator.sum(str), is(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {",3:2", ",,3:2", "3, :2"})
    @DisplayName("구분자 사이에 숫자가 존재하지 않는 경우, 0으로 판단하여 계산한다.")
    public void emptyNum(String input) {
        assertThat(stringCalculator.sum(input), is(5));
    }

    @Test
    @DisplayName("문자열 제일 앞 //와 \n 사이에 char1 짜리 커스텀 구분자를 지정할 수 있다")
    public void customSeparator() {
        assertThat(stringCalculator.sum("//;\n1;2;3"), is(6));
        assertThat(stringCalculator.sum("//;\n"), is(0));
        assertThat(stringCalculator.sum("//;\n1,2:3;4"), is(10));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1,2,*", "&"})
    @DisplayName("숫자가 아닌 문자열이 포함된다면 RuntimeException")
    public void nonNumberString_RuntimeException(String input) {
        assertThrows(RuntimeException.class, () -> stringCalculator.sum(input));
    }

    @Test
    @DisplayName("음수인 숫자가 전달 된다면 RuntimeException")
    public void negativeNum_RuntimeException() {
        assertThrows(RuntimeException.class, () -> stringCalculator.sum("1,-2"));
    }
}