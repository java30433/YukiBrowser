apply(from = "./secret.gradle.kts")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "bakuen.app.yukibrowser"
    compileSdk = 34

    defaultConfig {
        applicationId = "bakuen.app.yukibrowser"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "beta-0.0.1"
    }
    signingConfigs {
        create("release") {
            @Suppress("UNCHECKED_CAST")
            fun <T> ext(key: String) = rootProject.extra[key] as T
            storeFile = ext("storeFile")
            storePassword = ext("storePassword")
            keyAlias = ext("keyAlias")
            keyPassword = ext("keyPassword")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["release"]
        }
        debug {
            signingConfig = signingConfigs["release"]
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    implementation(fileTree("libs"))
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.wear:wear-tooling-preview:1.0.0")
    val fuelVersion = "2.3.1"
    implementation("com.github.kittinunf.fuel:fuel:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-android:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:$fuelVersion")
    debugImplementation("androidx.compose.ui:ui-tooling")
}