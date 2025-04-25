package com.respire.baseapp.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.respire.baseapp.ui.navigation.ScaleTransitionDirection
import com.respire.baseapp.ui.navigation.Screen
import com.respire.baseapp.ui.navigation.scaleIntoContainer
import com.respire.baseapp.ui.navigation.scaleOutOfContainer
import com.respire.baseapp.ui.selectZodiac.SelectZodiacScreen
import com.respire.baseapp.ui.theme.BaseAppTheme
import com.respire.baseapp.ui.theme.DarkBackground
import com.respire.baseapp.ui.theme.LightBackground
import com.respire.baseapp.ui.themeScreen.ThemeMode
import com.respire.baseapp.ui.themeScreen.ThemeScreen
import com.respire.baseapp.ui.zodiacDetails.ZodiacDetailsScreen
import com.respire.baseapp.ui.zodiacMain.ZodiacMainScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: BaseViewModel by viewModels<BaseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                LightBackground.toArgb(),
                DarkBackground.toArgb()
            ),
            statusBarStyle = SystemBarStyle.auto(
                LightBackground.toArgb(),
                DarkBackground.toArgb()
            )
        )
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.screenState.collect { startScreen ->
                    startScreen?.let {
                        content(startScreen)
                    }
                }
            }
        }
    }

    private fun content(startScreen: Screen) {
        setContent {
            val navController = rememberNavController()
            val appTheme by viewModel.themeState.collectAsState()

            BaseAppTheme(
                darkTheme = when (appTheme) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                }
            ) {
                NavHost(
                    navController = navController,
                    startDestination = startScreen,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background)
                ) {
                    composable<Screen.ZodiacList>(enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )
                    },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(500)
                            )
                        }) {
                        SelectZodiacScreen {
                            navController.navigate(Screen.ZodiacMain) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    composable<Screen.ZodiacMain>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(500)
                            )
                        }) { navBackResult ->
                        ZodiacMainScreen(
                            appTheme,
                            onSelectZodiac = {
                                navController.navigate(Screen.ZodiacList)
                            }, onAppThemeClick = {
                                navController.navigate(Screen.ThemeScreen)
                            }, onZodiacDetailsClick = { details ->
                                navController.navigate(Screen.ZodiacDetailsScreen(details))
                            })
                    }
                    composable<Screen.ThemeScreen>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(500)
                            )
                        }) {
                        ThemeScreen(appTheme, { themeMode ->
                            viewModel.updateTheme(themeMode)
//                            navController.previousBackStackEntry
//                                ?.savedStateHandle
//                                ?.set("themeMode", themeMode)
                        }, {
                            navController.popBackStack()
                        })
                    }

                    composable<Screen.ZodiacDetailsScreen>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(500)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(500)
                            )
                        }) {
                        ZodiacDetailsScreen()
                    }
                }
            }
        }
    }
}
