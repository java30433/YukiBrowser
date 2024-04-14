apply(from = "./secret.gradle.kts")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
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
    //implementation("io.github.jonanorman.android.webviewup:core:0.1.0")
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.5")
    implementation(project(":navigator"))
    implementation(project(":http"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.6.3")
    debugImplementation("androidx.compose.ui:ui-tooling")
}