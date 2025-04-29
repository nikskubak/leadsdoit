package com.androsuperbooster.horoscope.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import com.androsuperbooster.horoscope.R
import com.androsuperbooster.horoscope.data.AppLinks
import kotlinx.serialization.SerialName
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
    @Serializable
    class ZodiacDetailsScreen(@SerialName(AppLinks.LINK_PARAM)
                              var details : String) : Screen()
}

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 90))
}

enum class ScaleTransitionDirection {
    INWARDS, OUTWARDS
}
