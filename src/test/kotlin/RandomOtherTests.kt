/*
 * This file is a part of Cruvná.
 * Copyright (C) 2024 Rhys and Sam. All rights reserved.
 */

import org.ryecountryday.samandrhys.cruvna.util.*
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RandomOtherTests {
    @Test
    fun `Double#roundToTwoDecimalPlaces()`() {
        assertEquals(1.2345.roundToTwoDecimalPlaces(), 1.23)
        assertEquals(2.3456.roundToTwoDecimalPlaces(), 2.35)
    }

    @Test
    fun `Double#toMoneyString()`() {
        assertEquals(3.5.toMoneyString(), "3.50")
        assertEquals(3.0.toMoneyString(), "3.00")
        assertEquals(1.0E7.toMoneyString(), "10000000.00")
        assertEquals(1.2345.toMoneyString(), "1.23")
        assertEquals(2.3456.toMoneyString(), "2.35")
    }

    @Test
    fun `String#isPositiveDouble()`() {
        assertTrue("1.23".isPositiveDouble())
        assertTrue("0".isPositiveDouble())
        assertFalse("a".isPositiveDouble())
        assertFalse("-4.5".isPositiveDouble())
    }

    @Test
    fun `String#isValidMoneyString()`() {
        assertTrue("1.23".isValidMoneyString())
        assertTrue("100000".isValidMoneyString())
        assertTrue("100000.14".isValidMoneyString())
        assertFalse("100000.143".isValidMoneyString())
        assertFalse("-123.23".isValidMoneyString())
        assertFalse("not valid".isValidMoneyString())
    }

    @Test
    fun `parseDate(String)`() {
        assertEquals(parseDate("01/01/2024"), LocalDate.of(2024, 1, 1))
        assertEquals(parseDate("01-01-2024"), LocalDate.of(2024, 1, 1))
        assertEquals(parseDate("01.01.2024"), LocalDate.of(2024, 1, 1))

        assertEquals(parseDate("1/1/-99999"), LocalDate.of(-99999, 1, 1))
    }
}