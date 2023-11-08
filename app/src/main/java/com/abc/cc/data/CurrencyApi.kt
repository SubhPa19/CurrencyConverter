package com.abc.cc.data

import com.abc.cc.data.model.Currencies
import com.abc.cc.data.model.Latest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyApi {

    @Headers("Cache-Control: max-age=1800")
    @GET("latest.json")
    suspend fun getLatestRates(@Query("app_id") app_id: String): Response<Latest>

    @Headers("Cache-Control: max-age=1800")
    @GET("currencies.json")
    suspend fun getCurrencies(): Response<Currencies>
}