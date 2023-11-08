package com.abc.cc.data

import com.abc.cc.data.model.Currencies
import com.abc.cc.data.model.Latest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("latest.json")
    suspend fun getLatestRates(@Query("app_id") app_id: String): Response<Latest>

    @GET("currencies.json")
    suspend fun getCurrencies(): Response<Currencies>
}