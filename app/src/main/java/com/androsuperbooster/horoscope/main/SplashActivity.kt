package com.androsuperbooster.horoscope.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.androsuperbooster.horoscope_feature.ui.base.MainActivity

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startActivity(Intent(this, MainActivity::class.java))
    }
}