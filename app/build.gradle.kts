plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

@Suppress("UnstableApiUsage") android {
    namespace = "com.xjx.kotlin"
    compileSdk = Config.compileSdk

    defaultConfig {
        applicationId = "com.xjx.kotlin"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdkVersion
        versionCode = Config.versionCode
        versionName = Config.versionName
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
    }
}

dependencies {
    implementation(project(":apphelper"))
    implementation(project(":apphelper2"))
    implementation(project(":http"))
    implementation(project(":refresh"))
    implementation(project(":common"))

    implementation(Libs.appcompat)
    implementation(Libs.material)
    implementation(Libs.constraintlayout)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation(Libs.junit)
    androidTestImplementation(Libs.androidx_junit)
    androidTestImplementation(Libs.espresso_core)

    implementation(Libs.viewpager2)
    implementation(Libs.retrofit)
    implementation(Libs.adapter_rxjava2)
    implementation(Libs.converter_gson)
    implementation(Libs.converter_scalars)
    implementation(Libs.rxpermissions)
    implementation(Libs.rxjava3)

    implementation(Libs.kotlinx_coroutines_core)
    implementation(Libs.lifecycle_runtime_ktx)
    implementation(Libs.lifecycle_viewmodel_ktx)

    implementation(Libs.kotlin_reflect)

    // 反射
    implementation(Libs.activity_ktx)
    implementation(Libs.fragment_ktx)
    implementation(Libs.jeromq)
    implementation(files("libs/Cariad_CDA_AudioRecorder_release_202302031659.aar"))

    implementation("com.tencent.bugly:crashreport:latest.release")
}