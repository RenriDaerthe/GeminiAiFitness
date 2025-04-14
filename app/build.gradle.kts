plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization) // ✅ Serialization plugin added
}

android {
    namespace = "com.renri.geminiaifitness"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.renri.geminiaifitness"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Updated for Compose version compatibility
    }
}

dependencies {
    // OkHttp for logging network requests
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.5.1") // Compose UI
    implementation("androidx.compose.material3:material3:1.1.2") // Material3 for Compose
    implementation("androidx.navigation:navigation-compose:2.7.2") // Compose Navigation

    // LiveData and StateFlow support for Compose
    implementation("androidx.compose.runtime:runtime-livedata:1.5.1")  // For LiveData support in Compose
    implementation("androidx.compose.runtime:runtime:1.5.1")  // Core runtime for Compose

    // DataStore for preferences storage
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Preview Support
    debugImplementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")

    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coroutines for async tasks
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Jetpack Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Unit and Android Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
