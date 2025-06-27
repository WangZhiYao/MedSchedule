pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "MedSchedule"
include(":app")

include(":core:data")
include(":core:domain")
include(":core:common")

include(":shared:designsystem")
include(":shared:ui")

include(":feature:home")
include(":feature:medication-plan")
include(":feature:medication")
include(":feature:medication-record")