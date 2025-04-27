plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.timelab"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.timelab"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    // Material Design Components (для FloatingActionButton, BottomNavigationView, BottomSheetDialog)
    implementation (libs.material.v1110)

// AppCompat (базовая поддержка совместимости)
    implementation (libs.androidx.appcompat.v161)

// RecyclerView (список задач)
    implementation ("androidx.recyclerview:recyclerview:1.3.2")

// Fragment support
    implementation ("androidx.fragment:fragment-ktx:1.6.2")

// ConstraintLayout (если используется в layout'ах)
    implementation (libs.androidx.constraintlayout.v214)
    implementation (libs.material.v140)


    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.room:room-runtime:2.4.2")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.gcm)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}