@Suppress("DSL_SCOPE_VIOLATION") //  Remove once KTIJ-19369 is fixed
plugins {
	alias(libs.plugins.com.android.application)
	id("org.jetbrains.kotlin.android")
}

android {
	namespace = "com.xjx.kotlin"
	compileSdk = libs.versions.compileSdks.get().toInt()

	defaultConfig {
		applicationId = "com.xjx.kotlin"
		minSdk = libs.versions.minSdk.get().toInt()
		//noinspection ExpiredTargetSdkVersion
		targetSdk = libs.versions.targetSdk.get().toInt()
		versionCode = 1
		versionName = "1.0.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		ndk {
			//noinspection ChromeOsAbiSupport
			abiFilters += listOf("arm64-v8a", "x86", "x86_64")
		}

		buildConfigField("Boolean", "LogSwitch", "true")
	}

	signingConfigs {
		create("test") {
			// storeFile file("/Users/dasouche/StudioWorks/App/jks/apphelper.jks")
			storeFile = file("../jks/apphelper.jks")
			storePassword = "123456"
			keyPassword = "123456"
			keyAlias = "apphelper"
		}
		create("config") {
			// storeFile file("/Users/dasouche/StudioWorks/App/jks/apphelper.jks")
			storeFile = file("../jks/apphelper.jks")
			storePassword = "123456"
			keyAlias = "apphelper"
			keyPassword = "123456"
		}
	}

	buildTypes {
		release {
			isDebuggable = true
			signingConfig = signingConfigs.getByName("config")
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
		debug {
			signingConfig = signingConfigs.getByName("test")
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	kotlinOptions {
		jvmTarget = "1.8"
	}

	buildFeatures {
		viewBinding = true
		buildConfig = true
		aidl = true
	}

	configurations.all {
		resolutionStrategy {
			force(libs.recyclerview)
			force(libs.okhttp3)
			force(libs.rxjava2)
		}
	}
}

dependencies {
	implementation(project(":apphelper"))
	implementation(project(":refresh"))
	implementation(project(":http"))
	implementation(project(":common"))

	implementation(libs.appcompat)
	implementation(libs.material)
	implementation(libs.constraintlayout)
	implementation(libs.legacy.support.v4)
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("com.google.android.material:material:1.9.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	testImplementation(libs.junit)
	androidTestImplementation(libs.espresso.core)
	implementation(libs.viewpager2)

	implementation(libs.rxpermissions)
	implementation(libs.rxjava3)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.lifecycle.runtime.ktx)
	implementation(libs.lifecycle.viewmodel.ktx)
	implementation(libs.kotlin.reflect)
	// 反射
	implementation(libs.activity.ktx)
	implementation(libs.fragment.ktx)
	implementation(libs.jeromq)
	implementation(libs.crashreport)
	implementation(libs.player)

	implementation("com.airbnb.android:lottie:6.2.0")
}
