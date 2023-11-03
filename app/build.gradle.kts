@Suppress("DSL_SCOPE_VIOLATION") plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.xjx.kotlin"
    compileSdk = libs.versions.compileSdks.get().toInt()

    defaultConfig {
        applicationId = "com.xjx.kotlin"
        minSdk = libs.versions.minSdk.get().toInt()
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 28
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("test")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }
}

dependencies {
    implementation(project(":apphelper"))
    implementation(project(":apphelper2"))
    implementation(project(":http"))
    implementation(project(":refresh"))
    implementation(project(":common"))

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.viewpager2)
    implementation(libs.retrofit2)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
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
    // implementation(files("libs/Cariad_CDA_AudioRecorder_release_202302031659.aar"))

    implementation("com.tencent.bugly:crashreport:latest.release")
}