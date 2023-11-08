package com.abc.cc.data.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Latest(
    @SerializedName("base")
    val base: String? = null,
    @SerializedName("disclaimer")
    val disclaimer: String?,
    @SerializedName("license")
    val license: String?,
    @SerializedName("rates")
    val rates: Rates?,
    @SerializedName("timestamp")
    val timestamp: Int?
) {
    constructor() : this(null, null, null, null, null)
}