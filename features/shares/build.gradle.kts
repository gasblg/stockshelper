plugins {
    alias(libs.plugins.stockshelper.android.features)
    alias(libs.plugins.stockshelper.android.library)
    alias(libs.plugins.stockshelper.android.hilt)
    alias(libs.plugins.stockshelper.android.compose)
}

android {
    namespace = "com.github.gasblg.stockshelper.shares"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:models"))
    implementation(project(":core:common"))
    implementation(project(":core:design"))
    implementation(project(":core:preferences"))
    implementation(project(":features:sort"))
    implementation(project(":features:settings"))

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}