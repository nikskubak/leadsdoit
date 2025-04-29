package com.androsuperbooster.horoscope.ui.main

import com.androsuperbooster.horoscope.domain.model.CurrencyEntity

data class MainUi(val currencies: List<CurrencyEntity> = emptyList(), val lastUpdateTime : String = "")