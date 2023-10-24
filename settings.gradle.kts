pluginManagement {
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
        // 添加MPAndroidChart远程依赖
        maven { url=uri("https://jitpack.io") }
    }
}

rootProject.name = "tally"
include(":app")
 