plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.serialization)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.androsuperbooster.horoscope"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.androsuperbooster.horoscope"
        minSdk = 24
        targetSdk = 35
        versionCode = 6
        versionName = "1.0.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = file("$rootDir/keystore/debug.keystore")
            storePassword = "android"
        }
        create("release") {
            keyAlias = "horoscope"
            keyPassword = "horoscope"
            storeFile = file("$rootDir/keystore/horoscope-keystore.jks")
            storePassword = "horoscope"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_URL", "\"https://pro-api.coinmarketcap.com/\"")
            buildConfigField("String", "API_KEY", "\"5fc54438-353f-48ae-8487-a49d9e3d3310\"")
        }
        debug {
            buildConfigField("String", "API_URL", "\"https://sandbox-api.coinmarketcap.com/\"")
            buildConfigField("String", "API_KEY", "\"b54bcf4d-1bca-4e8e-9a24-22ff2c3d462c\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        compose = true
        android.buildFeatures.buildConfig = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature-horoscope"))
    implementation(project(":mvi"))

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.dagger.compiler)
}