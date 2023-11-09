package com.abc.cc.util

import com.abc.cc.util.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DispatchersProviderTest {

    @Mock
    private lateinit var mockCoroutineDispatcher: CoroutineDispatcher

    private lateinit var dispatchersProvider: DispatchersProvider

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dispatchersProvider = object : DispatchersProvider {
            override val main: CoroutineDispatcher = mockCoroutineDispatcher
            override val io: CoroutineDispatcher = mockCoroutineDispatcher
            override val default: CoroutineDispatcher = mockCoroutineDispatcher
            override val unconfined: CoroutineDispatcher = mockCoroutineDispatcher
        }
    }

    @Test
    fun testDispatchersProvider_Main() {
        Assert.assertEquals(mockCoroutineDispatcher, dispatchersProvider.main)
    }

    @Test
    fun testDispatchersProvider_IO() {
        Assert.assertEquals(mockCoroutineDispatcher, dispatchersProvider.io)
    }

    @Test
    fun testDispatchersProvider_Default() {
        Assert.assertEquals(mockCoroutineDispatcher, dispatchersProvider.default)
    }

    @Test
    fun testDispatchersProvider_Unconfined() {
        Assert.assertEquals(mockCoroutineDispatcher, dispatchersProvider.unconfined)
    }
}
