package com.respire.baseapp.ui.main

import com.respire.baseapp.domain.model.CurrencyEntity

data class MainUi(val currencies: List<CurrencyEntity> = emptyList(), val lastUpdateTime : String = "")