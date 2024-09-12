package org.ryecountryday.samandrhys.epm.util

import kotlin.math.round

fun Double.roundToTwoDecimalPlaces(): Double {
    return round(this * 100) / 100
}

fun Double.toMoneyString(): String {
    var result = this.roundToTwoDecimalPlaces().toString()
    if(result.substringAfter('.').length == 1) {
        result += "0"
    }
    return result
}

fun String.isPositiveDouble(): Boolean {
    return toDoubleOrNull()?.let { it >= 0 } == true
}

fun String.numDecimalPlaces(): Int {
    return this.indexOf('.').let { if (it == -1) 0 else length - it - 1 }
}

fun String.isValidMoneyString(): Boolean {
    return isPositiveDouble() && !endsWith("f") && !endsWith("F") && !endsWith("d") && !endsWith("D") && numDecimalPlaces() <= 2
}