package com.androsuperbooster.horoscope_feature.ui.main

import com.androsuperbooster.horoscope_feature.domain.model.CurrencyEntity

data class MainUi(val currencies: List<CurrencyEntity> = emptyList(), val lastUpdateTime : String = "")