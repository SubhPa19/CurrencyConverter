package com.abc.cc.data.repo

import android.content.Context
import com.abc.cc.data.CurrencyApi
import com.abc.cc.data.model.Currencies
import com.abc.cc.data.model.Latest
import com.abc.cc.util.Resources
import javax.inject.Inject

private const val API_KEY = "04125c27fcd34f1794bacd87d38db8fa"

class CurrencyRepository @Inject constructor(private val api: CurrencyApi) : Repository {
    override suspend fun getLatestRates(): Resources<Latest> {
        return try {
            val response = api.getLatestRates(API_KEY)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resources.Success(result)
            } else {
                Resources.Error(response.message())
            }

        } catch (e: Exception) {
            Resources.Error(message = e.message ?: "An Error Occurred")
        }
    }

    override suspend fun getCurrencies(): Resources<Currencies> {
        return try {
            val response = api.getCurrencies()
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resources.Success(result)
            } else {
                Resources.Error(response.message())
            }
        } catch (e: Exception) {
            Resources.Error(message = e.message ?: "An Error Occurred")
        }
    }
}