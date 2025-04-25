package com.respire.baseapp.domain.model

data class ZodiacEntity(
    val id: String?,
    val name: String?,
    val birthPeriod: String?,
    val dailyHoroscope: List<String>?,
    var animationRes: Int?,
    var todayHoroscope : String,
    var action: Action? = null
)

data class Action(val id: String?,
                  val details: String?,
                  val action: String?)
