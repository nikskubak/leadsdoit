plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.respire.mvi"
    compileSdk = 34

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
            buildConfigField("String", "API_KEY", "\"4e5d8d662816e9fbb308967503ab926c\"")
            buildConfigField("String", "API_HOST", "\"v3.football.api-sports.io\"")
            buildConfigField("String", "URL", "\"https://v3.football.api-sports.io/\"")
        }
        debug {
            buildConfigField("String", "API_KEY", "\"4e5d8d662816e9fbb308967503ab926c\"")
            buildConfigField("String", "API_HOST", "\"v3.football.api-sports.io\"")
            buildConfigField("String", "URL", "\"https://v3.football.api-sports.io/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(project(":core"))

    //Room
    ksp(libs.room.compiler)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.dagger.compiler)
}