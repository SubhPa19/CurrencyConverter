package com.abc.cc.data

import com.abc.cc.data.CurrencyApi
import com.abc.cc.data.model.Currencies
import com.abc.cc.data.model.Latest
import com.abc.cc.data.repo.CurrencyRepository
import com.abc.cc.util.Resources
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class CurrencyRepositoryTest {

    @Mock
    private lateinit var mockApi: CurrencyApi

    private lateinit var currencyRepository: CurrencyRepository

    private val API_KEY = "04125c27fcd34f1794bacd87d38db8fa"

    @Mock
    private lateinit var currencies: Currencies

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        currencyRepository = CurrencyRepository(mockApi)
    }

    @Test
    fun testGetLatestRates_Success() = runBlocking {
        // Mock data
        val mockLatestRates = Latest(/* your mock data */)
        val mockResponse = Response.success(mockLatestRates)
        `when`(mockApi.getLatestRates(API_KEY)).thenReturn(mockResponse)

        // Call the function
        val result = currencyRepository.getLatestRates()

        // Assert
        Assert.assertTrue(result is Resources.Success)
        Assert.assertEquals(mockLatestRates, (result as Resources.Success).data)
    }


    @Test
    fun testGetLatestRates_Exception() = runBlocking {
        // Mock exception
        `when`(mockApi.getLatestRates(API_KEY)).thenAnswer { throw RuntimeException("Network error") }

        // Call the function
        val result = currencyRepository.getLatestRates()

        // Assert
        Assert.assertTrue(result is Resources.Error)
        Assert.assertEquals("Network error", (result as Resources.Error).message)
    }

    @Test
    fun testGetCurrencies_Success() = runBlocking {
        // Mock data
        val mockCurrencies = currencies
        val mockResponse = Response.success(mockCurrencies)
        `when`(mockApi.getCurrencies()).thenReturn(mockResponse)

        // Call the function
        val result = currencyRepository.getCurrencies()

        // Assert
        Assert.assertTrue(result is Resources.Success)
        Assert.assertEquals(mockCurrencies, (result as Resources.Success).data)
    }

    @Test
    fun testGetCurrencies_Exception() = runBlocking {
        // Mock exception
        `when`(mockApi.getCurrencies()).thenAnswer { throw RuntimeException("Network error") }

        // Call the function
        val result = currencyRepository.getCurrencies()

        // Assert
        Assert.assertTrue(result is Resources.Error)
        Assert.assertEquals("Network error", (result as Resources.Error).message)
    }
}
