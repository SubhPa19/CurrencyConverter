package com.abc.cc.util

import android.content.Context
import com.abc.cc.data.CurrencyApi
import com.abc.cc.data.repo.CurrencyRepository
import com.abc.cc.di.AppModule
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.io.File

class AppModuleTest {

    @Mock
    private lateinit var mockContext: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testProvideDispatchers() {
        // Test the provideDispatchers function
        val dispatchersProvider = AppModule.provideDispatchers()

        // Assert
        Assert.assertNotNull(dispatchersProvider)
    }

    @Test
    fun testProvideCurrencyRepository() {
        // Mock data
        val mockCurrencyApi = mock(CurrencyApi::class.java)

        // Test the provideCurrencyRepository function
        val repository = AppModule.provideCurrencyRepository(mockCurrencyApi)

        // Assert
        Assert.assertNotNull(repository)
        Assert.assertTrue(repository is CurrencyRepository)
    }

    @Test
    fun testProvideOnlineInterceptor() {
        // Test the provideOnlineInterceptor function
        val interceptor = AppModule.provideOnlineInterceptor()

        // Assert
        Assert.assertNotNull(interceptor)
    }
}
