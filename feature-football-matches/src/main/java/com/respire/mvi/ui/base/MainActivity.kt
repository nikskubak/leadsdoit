package com.respire.mvi.ui.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.respire.mvi.data.AppLinks
import com.respire.mvi.ui.base.ui.theme.BaseAppTheme
import com.respire.mvi.ui.base.ui.theme.ThemeMode
import com.respire.mvi.ui.screens.mainScreen.MainScreen
import com.respire.mvi.ui.navigation.Screen
import com.respire.mvi.ui.screens.privacyScreen.PrivacyScreen
import com.respire.mvi.ui.screens.themeScreen.ThemeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    val viewModel: BaseViewModel by viewModels<BaseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        requestPermissioan()
        showContent(Screen.MainScreen)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val request = NavDeepLinkRequest.Builder
            .fromUri(Uri.parse(intent.data.toString()))
            .build()

        navController.navigate(
            request,
            navOptions = NavOptions.Builder().setLaunchSingleTop(true).build()
        )
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


    private fun showContent(startScreen: Screen) {
        setContent {
            navController = rememberNavController()
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
                    composable<Screen.MainScreen>(enterTransition = {
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
                        MainScreen(
                            appTheme,
                            onThemeClicked = {
                                navController.navigate(Screen.ThemeScreen)
                            },
                            onPrivacyClicked = { url ->
                                navController.navigate(Screen.PrivacyScreen(url))
                            },
                            onMatchClicked = {

                            }
                        )
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

                    composable<Screen.PrivacyScreen>(deepLinks = listOf(
                        navDeepLink<Screen.PrivacyScreen>(
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
                            backStackEntry.toRoute<Screen.PrivacyScreen>()
                        Log.e("backStackEntryRoute", backStackEntryRoute.details)
                        PrivacyScreen({
                            navController.popBackStack()
                        }, backStackEntryRoute.details)
                    }
                }
            }
        }
    }
}