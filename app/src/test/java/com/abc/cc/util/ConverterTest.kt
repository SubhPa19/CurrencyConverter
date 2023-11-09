package com.abc.cc.util

import com.abc.cc.data.model.Currencies
import com.abc.cc.data.model.Latest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ConverterTest {

    @Mock
    private lateinit var mockCurrencies: Currencies

    @Mock
    private lateinit var mockLatestRates: Latest


    @Mock
    private lateinit var mockTestData: TestData

    data class TestData(val name: String, val age: Int)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testMargeAPIResponse_withValidData() {
        val converter = Converter()

        // Mock data
        val currencyMapping = mockCurrencies
        val latestRates = mockLatestRates

        // Mock behavior
        `when`("latestRates.RATES").thenReturn(mapOf("USD" to 1.0, "EUR" to 0.85, "GBP" to 0.75).toString())

        val result = converter.margeAPIResponse(currencyMapping, latestRates)

        // Assert
        Assert.assertEquals(3, result.size)
        Assert.assertTrue(result.any { it.name == "USD" })
        Assert.assertTrue(result.any { it.name == "EUR" })
        Assert.assertTrue(result.any { it.name == "GBP" })
    }

    @Test
    fun testConvertRate_withValidData() {
        val exchangeRate = 0.85
        val selectedCurrency = 1.0
        val amountToConvert = "100"

        val result = convertRate(exchangeRate, selectedCurrency, amountToConvert)

        // Assert
        Assert.assertEquals("85", result)
    }

    @Test
    fun testConvertRate_withInvalidData() {
        val exchangeRate = 0.85
        val selectedCurrency = 0.0 // to cause division by zero
        val amountToConvert = "100"

        val result = convertRate(exchangeRate, selectedCurrency, amountToConvert)

        // Assert
        Assert.assertEquals("∞", result)
    }

    @Test
    fun testToMap_withValidData() {
        // Initialize mockito
        MockitoAnnotations.initMocks(this)

        // Mock data
        `when`(mockTestData.name).thenReturn("John")
        `when`(mockTestData.age).thenReturn(25)

        // Test the toMap function
        val result = toMap(mockTestData)

        // Assert
        Assert.assertEquals(2, result.size)
        Assert.assertEquals("John", result["NAME"])
        Assert.assertEquals(25, result["AGE"])
    }

    @Test
    fun testToMap_withNullProperty() {
        // Initialize mockito
        MockitoAnnotations.initMocks(this)

        // Mock data with a null property
        `when`(mockTestData.name).thenReturn(null)
        `when`(mockTestData.age).thenReturn(25)

        // Test the toMap function
        val result = toMap(mockTestData)

        // Assert
        Assert.assertEquals(2, result.size)
        Assert.assertNull(result["NAME"])
        Assert.assertEquals(25, result["AGE"])
    }


}

