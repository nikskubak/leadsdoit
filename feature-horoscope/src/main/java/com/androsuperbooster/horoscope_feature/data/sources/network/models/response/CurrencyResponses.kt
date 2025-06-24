package com.androsuperbooster.horoscope_feature.data.sources.network.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @Expose
    @SerializedName("response")
    val response: Int
)

data class CurrenciesMapResponse(
    @Expose
    @SerializedName("data") var data: ArrayList<CurrencyResponse> = arrayListOf(),
    @Expose
    @SerializedName("status") var status: Status? = Status()
)

data class Status(
    @Expose
    @SerializedName("timestamp") var timestamp: String? = null,
    @Expose
    @SerializedName("error_code") var errorCode: Int? = null,
    @Expose
    @SerializedName("error_message") var errorMessage: String? = null,
    @Expose
    @SerializedName("elapsed") var elapsed: Int? = null,
    @Expose
    @SerializedName("credit_count") var creditCount: Int? = null
)

data class CurrencyResponse(
    @Expose
    @SerializedName("id") var id: Int? = null,
    @Expose
    @SerializedName("rank") var rank: Int? = null,
    @Expose
    @SerializedName("name") var name: String? = null,
    @Expose
    @SerializedName("symbol") var symbol: String? = null,
    @Expose
    @SerializedName("slug") var slug: String? = null,
    @Expose
    @SerializedName("is_active") var isActive: Int? = null,
    @Expose
    @SerializedName("first_historical_data") var firstHistoricalData: String? = null,
    @Expose
    @SerializedName("last_historical_data") var lastHistoricalData: String? = null,
    @Expose
    @SerializedName("platform") var platform: String? = null
)
