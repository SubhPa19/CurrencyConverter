package com.abc.cc

import android.app.Application
import com.abc.cc.data.model.Latest
import com.abc.cc.util.MyInstanceCreator
import com.google.gson.GsonBuilder
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class CurrencyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val gsonBuilder = GsonBuilder()
        val instanceCreator = MyInstanceCreator()
        gsonBuilder.registerTypeAdapter(Latest::class.java, instanceCreator).setLenient()
    }
}