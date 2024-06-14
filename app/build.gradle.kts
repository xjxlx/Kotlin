@Suppress("DSL_SCOPE_VIOLATION") //  Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.xjx.kotlin"
    compileSdk =
        libs.versions.compileSdks
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.xjx.kotlin"
        // minSdk = libs.versions.minSdk.get().toInt()
        minSdk = 28
        //noinspection ExpiredTargetSdkVersion
        targetSdk =
            libs.versions.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            //noinspection ChromeOsAbiSupport
            abiFilters += listOf("arm64-v8a", "x86", "x86_64")
        }

        buildConfigField("Boolean", "LogSwitch", "true")
        vectorDrawables {
            useSupportLibrary = true
        }
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        aidl = true
        compose = true
    }

    configurations.all {
        resolutionStrategy {
            force(libs.recyclerview)
            force(libs.okhttp3)
            force(libs.rxjava2)
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
    implementation("androidx.activity:activity:1.9.0")

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
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("com.airbnb.android:lottie:6.2.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    // compose 依赖
    implementation("androidx.activity:activity-compose:1.7.0") // 使用compose activity
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics") // UI颜色的依赖
    implementation("androidx.compose.material3:material3") // compose 的组件
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // 预览工具
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling") // 工具

    // debugImplementation("com.squareup.leakcanary:leakcanary-android:2.13")
    // implementation("me.jessyan:autosize:1.2.1")
}
