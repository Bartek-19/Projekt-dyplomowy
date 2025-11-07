plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "pl.pollub.android.powerstrongapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "pl.pollub.android.powerstrongapp"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
}

dependencies {
    // AndroidX
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.lombok.mapstruct.binding)
    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.common)
    annotationProcessor(libs.room.compiler)
    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    annotationProcessor(libs.lifecycle.compiler)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    // Testy
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}