import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    kotlin("android")
    //id("com.jfrog.bintray")
}

android {
    compileSdk = 34
    defaultConfig {
//        applicationId = "com.patchself.compose.navigator"
        minSdk = 21

//        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled =  false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    namespace = "com.patchself.compose.navigator"
}

dependencies {
    compileOnly(platform("androidx.compose:compose-bom:2024.02.02"))
    compileOnly("androidx.compose.foundation:foundation")
}
