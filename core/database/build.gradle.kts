plugins {
    alias(libs.plugins.stockshelper.android.library)
    alias(libs.plugins.stockshelper.android.hilt)
    id("kotlinx-serialization")

}

android {
    namespace = "com.github.gasblg.stockshelper.core.database"

}

dependencies {
   implementation(project(":core:models"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlin.serialization)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}