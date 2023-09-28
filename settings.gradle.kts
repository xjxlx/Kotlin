pluginManagement {

    // 配置本地插件
    plugins {
        id("io.github.xjxlx.publish") version "1.0.0"
    }

    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://jitpack.io") }
        gradlePluginPortal()
        google()
        mavenLocal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)

    repositories {
        flatDir {
            dir("app/libs")
        }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://jitpack.io") }
        google()
        mavenCentral()
    }
}

rootProject.name = "Kotlin"
include(":app")
include(":apphelper")
include(":apphelper2")
include(":refresh")
include(":http")
include(":common")

include(":model")
include(":publish")
