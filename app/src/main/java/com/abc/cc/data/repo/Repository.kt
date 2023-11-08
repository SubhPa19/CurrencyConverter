package com.abc.cc.data.repo

import com.abc.cc.data.model.Currencies
import com.abc.cc.data.model.Latest
import com.abc.cc.util.Resources

interface Repository {

    suspend fun getLatestRates(): Resources<Latest>

    suspend fun getCurrencies(): Resources<Currencies>
}