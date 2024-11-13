plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.mogun.yotubeapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mogun.yotubeapp"
        minSdk = 28
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
        viewBinding = true
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.appcompat)

    implementation(libs.gson)
    implementation(libs.glide)

    // ExoPlayer 대신 Media3 라이브러리 추가
    implementation (libs.androidx.media3.exoplayer)
    implementation (libs.androidx.media3.ui) // UI PlayerView 사용 시 필요
    implementation (libs.androidx.media3.common) // 공통 구성 요소

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}