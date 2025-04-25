package com.respire.baseapp.ui.zodiacMain

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.respire.baseapp.ui.navigation.Screen
import com.respire.baseapp.ui.theme.BaseAppTheme
import com.respire.baseapp.ui.themeScreen.ThemeMode
import com.respire.baseapp.ui.zodiacMain.daily.DailyScreen
import com.respire.baseapp.ui.zodiacMain.settings.SettingsScreen


@Composable
fun ZodiacMainScreen(
    appTheme: ThemeMode,
    onSelectZodiac: () -> Unit,
    onAppThemeClick: () -> Unit,
    onZodiacDetailsClick: (String) -> Unit
) {
    var currentScreenIndex by rememberSaveable { mutableIntStateOf(0) }

    val items = listOf(
        Screen.Daily,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = currentScreenIndex == index,
                        onClick = { currentScreenIndex = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.iconRes ?: 0),
                                contentDescription = stringResource(screen.titleRes ?: 0)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(screen.titleRes ?: 0)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedContent(
                targetState = currentScreenIndex,
                transitionSpec = {
                    val direction = if (targetState == 0) {
                        AnimatedContentTransitionScope.SlideDirection.Right
                    } else {
                        AnimatedContentTransitionScope.SlideDirection.Left
                    }
                    slideIntoContainer(direction) togetherWith
                            slideOutOfContainer(direction)
                },
                label = "screen_transition"
            ) { currentScreenIndex ->
                when (currentScreenIndex) {
                    0 -> DailyScreen(onZodiacDetailsClick)
                    1 -> SettingsScreen(appTheme, onSelectZodiac, onAppThemeClick)
                    else -> {}
                }
            }
        }


//        Box(modifier = Modifier.padding(paddingValues)) {
//            when (currentScreen) {
//                Screen.Daily -> DailyScreen()
//                Screen.Settings -> SettingsScreen(onSelectZodiac, onAppThemeClick)
//                else -> {}
//            }
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun ZodiacMainScreenPreview() {
    BaseAppTheme(darkTheme = false, dynamicColor = false) {
        ZodiacMainScreen(ThemeMode.SYSTEM, {}, {}, {})
    }
}