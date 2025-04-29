package com.androsuperbooster.horoscope.domain.model

data class CurrencyEntity(
    val id: Int?,
    val rank: Int?,
    val name: String?,
    val symbol: String?,
    val slug: String?,
    val isActive: Int?,
    val firstHistoricalData: String?,
    val lastHistoricalData: String?,
    val platform: String?
)
