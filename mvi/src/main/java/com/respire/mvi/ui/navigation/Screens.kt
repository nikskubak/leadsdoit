package com.respire.mvi.ui.navigation

import com.respire.mvi.data.AppLinks
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object MainScreen : Screen()
    @Serializable
    data class PrivacyScreen(@SerialName(AppLinks.LINK_PARAM)
                                  var details : String) : Screen()
    @Serializable
    data object ThemeScreen : Screen()
}

