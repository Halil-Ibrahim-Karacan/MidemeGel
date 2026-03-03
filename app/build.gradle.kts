plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id ("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.midemegel"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.midemegel"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.gson)

    //viewmodel
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.activity.ktx)

    //livedata
    implementation (libs.androidx.lifecycle.livedata)
    kapt (libs.androidx.lifecycle.compiler)

    //hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)

    implementation(libs.androidx.hilt.navigation.compose)

    //coil(resim yüklemek için)
    implementation(libs.coil.compose)

    //Room
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)

    //Coroutine
    implementation (libs.androidx.room.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx.v2100)

    implementation(libs.accompanist.flowlayout)


}