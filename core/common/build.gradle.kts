plugins {
    alias(libs.plugins.stockshelper.android.library)
    alias(libs.plugins.stockshelper.android.hilt)
}

android {
    namespace = "com.github.gasblg.stockshelper.common"

}

dependencies {

    implementation(project(":core:design"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}