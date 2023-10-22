pluginManagement {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://jitpack.io") }
        gradlePluginPortal()
        google()
        jcenter()
        mavenLocal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        flatDir {
            dir("app/libs")
        }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://jitpack.io") }
        google()
        jcenter()
        mavenLocal()
        mavenCentral()
        maven {
            credentials {
                username = "6123a7974e5db15d52e7a9d8"
                password = "HsDc[dqcDfda"
            }
            setUrl("https://packages.aliyun.com/maven/repository/2131155-release-wH01IT/")
        }
        maven {
            credentials {
                username = "6123a7974e5db15d52e7a9d8"
                password = "HsDc[dqcDfda"
            }
            setUrl("https://packages.aliyun.com/maven/repository/2131155-snapshot-mh62BC/")
        }
    }
    versionCatalogs {
        create("libs") {
            from("com.android.version:catalog:1.0.0")
        }
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
