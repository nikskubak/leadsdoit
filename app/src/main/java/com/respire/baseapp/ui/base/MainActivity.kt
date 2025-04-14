package com.respire.baseapp.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.respire.baseapp.ui.addCurrency.AddCurrencyScreen
import com.respire.baseapp.ui.main.MainScreen
import com.respire.baseapp.ui.navigation.AddCurrency
import com.respire.baseapp.ui.navigation.Main
import com.respire.baseapp.ui.theme.BaseAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            BaseAppTheme {
                NavHost(navController = navController, startDestination = Main) {
                    composable<Main> { MainScreen() }
                    composable<AddCurrency> { AddCurrencyScreen() }
                }
            }
        }
    }
}
