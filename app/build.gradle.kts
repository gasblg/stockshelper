plugins {
    alias(libs.plugins.stockshelper.android.application)
    alias(libs.plugins.stockshelper.android.application.compose)
    alias(libs.plugins.stockshelper.android.hilt)

    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.github.gasblg.stockshelper"

    defaultConfig {
        applicationId = "com.github.gasblg.stockshelper"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        resourceConfigurations.plus(listOf("en", "ru"))

    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":features:shares"))
    implementation(project(":features:sort"))
    implementation(project(":features:settings"))
    implementation(project(":features:search"))
    implementation(project(":features:news"))
    implementation(project(":features:currencies"))
    implementation(project(":features:derivatives"))

    implementation(project(":core:data"))
    implementation(project(":core:models"))
    implementation(project(":core:common"))
    implementation(project(":core:design"))
    implementation(project(":core:preferences"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.appcompat)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.compose.material3.windowSizeClass)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

}
