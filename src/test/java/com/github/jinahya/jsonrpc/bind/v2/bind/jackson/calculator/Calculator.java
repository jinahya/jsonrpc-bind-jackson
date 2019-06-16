package com.github.jinahya.jsonrpc.bind.v2.bind.jackson.calculator;

/*-
 * #%L
 * jsonrpc-bind
 * %%
 * Copyright (C) 2019 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An interface for a calculator.
 *
 * @see <a href="https://www.factmonster.com/math-science/mathematics/terms-used-in-equations">Terms Used in
 * Equations</a>
 */
public interface Calculator {

    /**
     * Adds specified addend to specified augend and returns the result.
     *
     * @param addend the addend.
     * @param augend the augend.
     * @return the result of {@code augend + addend}.
     * @see BigDecimal#add(BigDecimal)
     * @see <a href="http://mathworld.wolfram.com/Addition.html">Addition (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Augend.html">Augend (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Addend.html">Addend (MathWorld)</a>
     */
    // argend + addend -> apply +addend to augend
    default BigDecimal add(final BigDecimal addend, final BigDecimal augend) {
        return addend.add(augend);
    }

    /**
     * Subtracts specified subtrahend from specified minuend.
     *
     * @param minuend    the minuend.
     * @param subtrahend the subtrahend.
     * @return the result of {@code minuend - subtrahend}.
     * @see BigDecimal#subtract(BigDecimal)
     * @see <a href="http://mathworld.wolfram.com/Subtraction.html">Subtraction (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Minuend.html">Minuend (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Subtrahend.html">Subtrahend (MathWorld)</a>
     */
    // A - B = A + (-B) => apply +(-B) to A
    // The difference from minuend to subtrahend
    default BigDecimal subtract(final BigDecimal minuend, final BigDecimal subtrahend) {
        return minuend.subtract(subtrahend);
    }

    /**
     * Returns the value of specified multiplicand multiplied by specified multiplier.
     *
     * @param multiplier   the multiplier.
     * @param multiplicand the multiplicand.
     * @return the value of {@code multiplicand} multiplied by {@code multiplier}.
     * @see BigDecimal#multiply(BigDecimal)
     * @see <a href="http://mathworld.wolfram.com/Multiplication.html">Multiplication (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Multiplier.html">Multiplier (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Multiplicand.html">Multiplicand (MathWorld)</a>
     */
    // multiplier times multiplicand
    default BigDecimal multiply(final BigDecimal multiplier, final BigDecimal multiplicand) {
        return multiplier.multiply(multiplicand);
    }

    /**
     * Returns the value of specified dividend divided by specified divisor.
     *
     * @param dividend the dividend.
     * @param divisor  the divisor.
     * @return the result of {@code dividend / divisor}.
     * @see BigDecimal#divide(BigDecimal)
     * @see <a href="http://mathworld.wolfram.com/Division.html">Division (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Dividend.html">Dividend (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Divisor.html">Divisor (MathWorld)</a>
     * @deprecated Use {@link #divide(BigDecimal, BigDecimal, RoundingMode)}
     */
    // the dividend is divided by the divisor to get a quotient
    @Deprecated
    default BigDecimal divide(final BigDecimal dividend, final BigDecimal divisor) {
        return dividend.divide(divisor);
    }

    /**
     * Returns the value of specified dividend divided by specified divisor using specified rounding mode.
     *
     * @param dividend     the dividend.
     * @param divisor      the divisor.
     * @param roundingMode the rounding mode.
     * @return the result of {@code dividend / divisor}.
     * @see BigDecimal#divide(BigDecimal, RoundingMode)
     * @see <a href="http://mathworld.wolfram.com/Division.html">Division (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Dividend.html">Dividend (MathWorld)</a>
     * @see <a href="http://mathworld.wolfram.com/Divisor.html">Divisor (MathWorld)</a>
     */
    // the dividend is divided by the divisor to get a quotient
    default BigDecimal divide(final BigDecimal dividend, final BigDecimal divisor, final RoundingMode roundingMode) {
        return dividend.divide(divisor, roundingMode);
    }
}
