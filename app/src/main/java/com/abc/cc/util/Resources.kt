package com.abc.cc.util

import com.abc.cc.data.model.Latest
import com.google.gson.InstanceCreator
import java.lang.reflect.Type


sealed class Resources<T>(val data: T?, val message: String?) {
    class Success<T>(data: T?) : Resources<T>(data, null)
    class Error<T>(message: String) : Resources<T>(null, message)
}

class MyInstanceCreator : InstanceCreator<Latest?> {
    override fun createInstance(type: Type?): Latest {
        return Latest()
    }
}