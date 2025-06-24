plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.serialization)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.androsuperbooster.horoscope_feature"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "HOROSCOPE_APP_ID", "\"com.androsuperbooster.horoscope\"")
        }
        debug {
            buildConfigField("String", "HOROSCOPE_APP_ID", "\"com.androsuperbooster.horoscope\"")
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

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.dagger.compiler)
}