package com.androsuperbooster.horoscope.ui.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.androsuperbooster.horoscope.data.AnalyticsKeys
import com.androsuperbooster.horoscope.data.AppLinks
import com.androsuperbooster.horoscope.ui.navigation.Screen
import com.androsuperbooster.horoscope.ui.selectZodiac.SelectZodiacScreen
import com.androsuperbooster.horoscope.ui.theme.BaseAppTheme
import com.androsuperbooster.horoscope.ui.theme.DarkBackground
import com.androsuperbooster.horoscope.ui.theme.LightBackground
import com.androsuperbooster.horoscope.ui.themeScreen.ThemeMode
import com.androsuperbooster.horoscope.ui.themeScreen.ThemeScreen
import com.androsuperbooster.horoscope.ui.zodiacDetails.ZodiacDetailsScreen
import com.androsuperbooster.horoscope.ui.zodiacMain.ZodiacMainScreen
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KType
import kotlin.reflect.typeOf

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
        requestPermissioan()
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

    private fun requestPermissioan() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Register the permission launcher
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                // Handle the result if needed
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
                                FirebaseAnalytics.getInstance(this@MainActivity)
                                    .logEvent(AnalyticsKeys.CLICK_BUTTON_DETAILS, null)
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
                        }, {
                            navController.popBackStack()
                        })
                    }

                    composable<Screen.ZodiacDetailsScreen>(deepLinks = listOf(
                        navDeepLink<Screen.ZodiacDetailsScreen>(
                            basePath = AppLinks.DETAILS,
                            typeMap = mapOf(
                                typeOf<String>() to NavType.StringType
                            )
                        ) {
                            uriPattern = AppLinks.DETAILS_WITH_PARAMS
                        }
                    ), typeMap = mapOf(
                        typeOf<String>() to NavType.StringType
                    ),
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
                        }) { backStackEntry ->

                        val backStackEntryRoute =
                            backStackEntry.toRoute<Screen.ZodiacDetailsScreen>()
                        Log.e("backStackEntryRoute", "${backStackEntryRoute.details}")
                        ZodiacDetailsScreen({
                            navController.popBackStack()
                        }, backStackEntryRoute.details)
                    }
                }
            }
        }
    }
}
