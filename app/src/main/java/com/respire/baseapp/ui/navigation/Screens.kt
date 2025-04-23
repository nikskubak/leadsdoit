package com.respire.baseapp.ui.navigation

import com.respire.baseapp.R
import kotlinx.serialization.Serializable

@Serializable
object Main
@Serializable
object AddCurrency

@Serializable
sealed class Screen(var titleRes : Int? = null, var iconRes : Int? = null) {
    @Serializable
    object Daily : Screen(R.string.daily, R.drawable.ic_daily)
    @Serializable
    object Settings : Screen(R.string.settings, R.drawable.ic_settings)
    @Serializable
    object ZodiacList : Screen()
    @Serializable
    object ZodiacMain : Screen()
    @Serializable
    object ThemeScreen : Screen()
}