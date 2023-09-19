buildscript {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/google") }
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://jitpack.io") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        // 发布JitPack的依赖版本，【3.0+以上用1.5】，【4.1+以上用2.0】，【4.6+以上用2.1】
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1") // Add this line
        classpath("com.android.tools.build:gradle:7.4.0")
        classpath(kotlin("gradle-plugin", version = "1.7.0"))
        classpath("org.javassist:javassist:3.26.0-GA")
    }
}

plugins {
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}