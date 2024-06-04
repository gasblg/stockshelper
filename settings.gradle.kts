pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "stockshelper"
include(":app")

include(":core:database")
include(":core:models")
include(":core:data")
include(":core:network")
include(":core:common")
include(":core:design")
include(":features:shares")
include(":core:preferences")
include(":features:settings")
include(":features:search")
include(":features:sort")
include(":features:news")
include(":features:currencies")
include(":features:derivatives")
