package com.abc.cc.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class NetworkUtilKtTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockConnectivityManager: ConnectivityManager

    @Mock
    private lateinit var mockNetworkInfo: NetworkInfo

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager)
    }

    @Test
    fun testIsInternetAvailable_withActiveNetwork() {
        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
        `when`(mockNetworkInfo.isConnected).thenReturn(true)

        val result = isInternetAvailable(mockContext)

        Assert.assertTrue(result)
    }

    @Test
    fun testIsInternetAvailable_withNoActiveNetwork() {
        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(null)

        val result = isInternetAvailable(mockContext)

        Assert.assertFalse(result)
    }

    @Test
    fun testIsInternetAvailable_withInactiveNetwork() {
        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
        `when`(mockNetworkInfo.isConnected).thenReturn(false)

        val result = isInternetAvailable(mockContext)

        Assert.assertFalse(result)
    }
}