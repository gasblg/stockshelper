import com.github.gasblg.stockshelper.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeaturesConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("stockshelper.android.library")
                apply("stockshelper.android.hilt")
            }

            dependencies {
                add("implementation", project(":core:design"))

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())

                add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling").get())

                //paging
                add("implementation", libs.findLibrary("androidx.paging.runtime").get())
                add("implementation", libs.findLibrary("androidx.paging.compose").get())
            }
        }
    }
}
