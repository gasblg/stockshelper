plugins {
    alias(libs.plugins.stockshelper.android.library)
    alias(libs.plugins.stockshelper.android.hilt)
    id("kotlinx-serialization")

}

android {
    namespace = "com.github.gasblg.stockshelper.network"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:models"))
    implementation(project(":core:database"))
    implementation(project(":core:common"))


    //kotlin
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    //paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}