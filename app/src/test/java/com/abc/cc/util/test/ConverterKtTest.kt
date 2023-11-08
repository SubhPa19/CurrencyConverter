package com.abc.cc.util.test


import org.junit.Test

class ConverterKtTest {

    @Test
    fun convertAnyToLinkedHashMap() {
    }

    @Test
    fun toMap() {
    }

    @Test
    fun convertRateTest() {
        val convertRate = com.abc.cc.util.convertRate(10.0, 1.0, "10 ").toInt()
        assert(convertRate == 100)
    }
    @Test
    fun convertRateEmptyAmountTest() {
        val convertRate = com.abc.cc.util.convertRate(10.0, 1.0, " ")
        assert(convertRate == "")
    }
    @Test
    fun convertRateDivideByZeroTest() {
        val convertRate = com.abc.cc.util.convertRate(10.0, 0.0, "10")
        assert(convertRate == "∞")
    }
    @Test
    fun convertZeroRateTest() {
        val convertRate = com.abc.cc.util.convertRate(0.0, 10.0, "10")
        assert(convertRate == "0")
    }
}