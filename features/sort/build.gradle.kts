plugins {
    alias(libs.plugins.stockshelper.android.features)
    alias(libs.plugins.stockshelper.android.library)
    alias(libs.plugins.stockshelper.android.hilt)
    alias(libs.plugins.stockshelper.android.compose)
}

android {
    namespace = "com.github.gasblg.stockshelper.sort"
}

dependencies {
    implementation(project(":core:preferences"))
    implementation(project(":core:models"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}