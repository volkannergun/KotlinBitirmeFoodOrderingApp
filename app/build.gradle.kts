plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.example.kotlinbitirmevolkanergunfoodorderingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kotlinbitirmevolkanergunfoodorderingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1") // Updated
    implementation("androidx.appcompat:appcompat:1.7.0") // Updated
    implementation("com.google.android.material:material:1.12.0") // Updated
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Stable, no major change needed unless specific feature desired

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0") // Updated
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0") // Updated
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0") // Updated

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7") // Already recent
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7") // Already recent

    implementation("com.squareup.retrofit2:retrofit:2.11.0") // Updated
    implementation("com.squareup.retrofit2:converter-gson:2.11.0") // Updated (to match Retrofit)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Updated (OkHttp3)

    implementation("com.github.bumptech.glide:glide:4.16.0") // Stable, no major urgent change for v4

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1") // Updated
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1") // Updated

    implementation("com.google.dagger:hilt-android:2.51.1") // Updated
    kapt("com.google.dagger:hilt-compiler:2.51.1") // Updated (to match Hilt)

    implementation("androidx.room:room-runtime:2.6.1") // Already recent
    ksp("androidx.room:room-compiler:2.6.1") // Already recent
    implementation("androidx.room:room-ktx:2.6.1") // Already recent

    implementation("com.airbnb.android:lottie:6.4.1") // Updated

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

kapt {
    correctErrorTypes = true
}