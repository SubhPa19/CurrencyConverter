package com.abc.cc.util

import com.abc.cc.data.model.Currencies
import com.abc.cc.data.model.ExchangeRates
import com.abc.cc.data.model.Latest
import java.text.DecimalFormat
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class Converter {
    fun margeAPIResponse(
        currencyMapping: Currencies,
        latestRates: Latest?
    ): MutableList<ExchangeRates> {
        val conversionList = mutableListOf<ExchangeRates>()
        val latestRatesMap = toMap(latestRates!!)
        val currencyHashMap = toMap(currencyMapping)
        val any: Any? = latestRatesMap["RATES"]
        val exchangeHashMap = convertAnyToLinkedHashMap(any)
        currencyHashMap.forEach { it ->
            val exchangeRate: Number = exchangeHashMap!!.getOrDefault(it.key, 0)
            val conversion = ExchangeRates(it.key, it.value.toString(), exchangeRate.toDouble())
            conversionList.add(conversion)
        }
        return conversionList
    }
}

fun convertAnyToLinkedHashMap(any: Any?): LinkedHashMap<String, Double>? {
    if (any is LinkedHashMap<*, *>) {
        val result = LinkedHashMap<String, Double>()
        for (entry in any) {
            if (entry.key is String && entry.value is Double) {
                result[entry.key as String] =
                    DecimalFormat("#.##").format(entry.value as Double).toDouble()
            }
        }
        return result
    }
    return null // Return null if the conversion is not possible
}

fun <T : Any> toMap(obj: T): Map<String, Any?> {
    return (obj::class as KClass<T>).memberProperties.associate { prop ->
        prop.name.uppercase() to prop.get(obj)?.let { value ->
            if (value::class.isData) {
                toMap(value)
            } else {
                value
            }
        }
    }
}

fun convertRate(exchangeRate: Double, selectedCurrency: Double, amountToConvert: Int): String {
    val convertedAmount = (exchangeRate / selectedCurrency).times(amountToConvert)
    return DecimalFormat("#.##").format(convertedAmount)
}
