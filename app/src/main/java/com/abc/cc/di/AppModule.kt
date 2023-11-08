package com.abc.cc.di

import android.content.Context
import com.abc.cc.data.CurrencyApi
import com.abc.cc.data.repo.CurrencyRepository
import com.abc.cc.data.repo.Repository
import com.abc.cc.util.DispatchersProvider
import com.abc.cc.util.isInternetAvailable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.*
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


private const val BASE_URL = "https://openexchangerates.org/api/"


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi(@ApplicationContext context: Context): CurrencyApi {
        val cacheSize = (10 * 1024 * 1024).toLong()
        val cache = Cache(context.cacheDir, cacheSize)
        val cacheHttpClient =
            OkHttpClient.Builder().addInterceptor(provideOfflineInterceptor(context)).addNetworkInterceptor(
                provideOnlineInterceptor()).cache(cache).build()
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(cacheHttpClient).build()
            .create(CurrencyApi::class.java)
    }


    @Singleton
    @Provides
    fun provideDispatchers(): DispatchersProvider = object : DispatchersProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    @Singleton
    @Provides
    fun provideCurrencyRepository(api: CurrencyApi): Repository = CurrencyRepository(api)

    private fun provideOnlineInterceptor() = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 1800
        response.newBuilder().header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma").build()
    }

    private fun provideOfflineInterceptor(context: Context) = Interceptor { chain ->
        var request: Request = chain.request()
        if (!isInternetAvailable(context = context)) {
            val maxStale = 60 * 60 * 24 * 1
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma").build()
        }
        chain.proceed(request)
    }

}