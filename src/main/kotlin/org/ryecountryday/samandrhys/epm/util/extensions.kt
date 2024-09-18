/*
 * This file is a part of EmployeePayManager.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

package org.ryecountryday.samandrhys.epm.util

import kotlin.math.round

/*
 * Different utility and extension functions, used throughout the program.
 */

/**
 * @return this [Double], rounded to two decimal places.
 */
fun Double.roundToTwoDecimalPlaces(): Double {
    return round(this * 100) / 100
}

/**
 * Converts this [Double] to a money-formatted string with exactly two decimal places.
 * If the number has fewer than two decimal places, trailing zeroes are added.
 *
 * For example, the result of `3.5.toMoneyString()` is `3.50`,
 * and the result of `3.0.toMoneyString()` is `3.00`.
 *
 * The result of this method does not contain any monetary signs, such as "$" or "£".
 * @return A string representation of the Double, rounded to two decimal places.
 */
fun Double.toMoneyString(): String {
    var result = this.roundToTwoDecimalPlaces().toString()
    result += "0".repeat(2 - result.numDecimalPlaces())
    return result
}

/**
 * @return if this String can be parsed as a [Double], and if so, if that `Double` is positive or equal to zero.
 */
fun String.isPositiveDouble(): Boolean {
    return toDoubleOrNull()?.let { it >= 0 } == true
}

/**
 * @return The number of digits after the decimal point (`.`) in this [String]. If there is no decimal point, returns 0.
 */
fun String.numDecimalPlaces(): Int {
    return this.indexOf('.').let { if (it == -1) 0 else length - it - 1 }
}

/**
 * Validates if this [String] represents a positive [Double] formatted as valid money.
 *
 * The String must represent a positive number with up to two decimal places,
 * and it must not end with `f` or `d`, case-insensitive.
 *
 * @return if this String is a valid money representation.
 */
fun String.isValidMoneyString(): Boolean {
    return isPositiveDouble()
            && !this[length - 1].lowercaseChar().let { it == 'd' || it == 'f' }
            && numDecimalPlaces() <= 2
}

fun <T> MutableCollection<T>.addAll(vararg elements: T) = addAll(elements)

fun addShutdownHook(block: () -> Unit) {
    Runtime.getRuntime().addShutdownHook(Thread(block))
}