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
    // implementation(project(":apphelper2"))
    implementation(project(":http"))
    implementation(project(":refresh"))
    implementation(project(":common"))

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.legacy.support.v4)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
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
    implementation(libs.crashreport)

    implementation("com.airbnb.android:lottie:6.2.0") {
        exclude(group = "androidx.fragment", module = "fragment")
        exclude(group = "androidx.fragment-ktx", module = "fragment")
        exclude(group = "androidx.activity", module = "activity")
        exclude(group = "androidx.activity-ktx", module = "activity")

        exclude(group = "androidx.appcompat", module = "appcompat-resources")
        exclude(group = "androidx.appcompat", module = "appcompat")
        exclude(group = "androidx.emoji2", module = "emoji2-views-helper")
        exclude(group = "androidx.emoji2", module = "emoji2")
        exclude(group = "androidx.core", module = "core-ktx")
        exclude(group = "androidx.core", module = "core")
        exclude(group = "androidx.annotation", module = "annotation-experimental")
    }
}