buildscript {
	repositories {
		maven { setUrl("https://plugins.gradle.org/m2/") }
	}

	repositories {
		maven { setUrl("https://maven.aliyun.com/repository/google") }
		maven { setUrl("https://maven.aliyun.com/repository/public") }
		maven { setUrl("https://maven.aliyun.com/repository/central") }
		maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
		maven { setUrl("https://jitpack.io") }
		google()
		mavenLocal() // 加载本地插件
		mavenCentral()
		gradlePluginPortal()
	}

	dependencies {
		// 发布JitPack的依赖版本，【3.0+以上用1.5】，【4.1+以上用2.0】，【4.6+以上用2.1】
		classpath(kotlin("gradle-plugin", version = "1.7.0"))
		classpath("org.javassist:javassist:3.26.0-GA")
	}
}

@Suppress("DSL_SCOPE_VIOLATION") // Remove once KTIJ-19369 is fixed
plugins {
	// id("com.android.application") version "7.4.2" apply false
	// id("com.android.library") version "7.4.2" apply false
	// id("org.jetbrains.kotlin.android") version "1.8.10" apply false
	alias(libs.plugins.com.android.application) apply false
	alias(libs.plugins.com.android.library) apply false
	alias(libs.plugins.org.jetbrains.kotlin.android) apply false
	alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
}
true
