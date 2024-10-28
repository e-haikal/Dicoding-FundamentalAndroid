plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.siaptekno.dicodingevent"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.siaptekno.dicodingevent"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // ViewBinding (for view management)
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit for networking
    implementation (libs.retrofit2.retrofit)
    implementation (libs.squareup.converter.gson)

    // OkHttp for networking
    implementation(libs.logging.interceptor)

    // Glide for image loading
    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    // Android Architecture Components (ViewModel, LiveData, KTX)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)

    // Navigation Component with KTX support
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    // Kotlin Coroutines
    implementation (libs.kotlinx.coroutines.android)

    // RecyclerView
    implementation (libs.androidx.recyclerview)

    // Kotlin KTX for various Android components
    implementation (libs.androidx.fragment.ktx)

    //room
    implementation(libs.androidx.room.runtime)
    ksp(libs.room.compiler)



}