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
    namespace = "com.respire.core"
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    testApi(libs.junit)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)
    androidTestApi(platform(libs.androidx.compose.bom))
    androidTestApi(libs.androidx.ui.test.junit4)
    debugApi(libs.androidx.ui.tooling)
    debugApi(libs.androidx.ui.test.manifest)

    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)

    //Room
    api(libs.room.ktx)
    api(libs.room.runtime)
    ksp(libs.room.compiler)

    //Retrofit
    api(libs.retrofit)
    api(libs.okhttp)
    api(libs.okhttp.logging)
    api(libs.gson)
    api(libs.retrofit.gson)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.dagger.compiler)

    //Coroutines
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)

    //Navigation
    api(libs.serialization)
    api(libs.navigation.compose)

    //Firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.config)
    api(libs.firebase.analytics)
    api(libs.firebase.inappmessaging)
    api(libs.firebase.cloudmessaging)

    //Lottie
//    implementation(libs.lottie.compose)

    //Hawk
    api(libs.hawk)

    //Install referrer
    api(libs.installreferrer)
}