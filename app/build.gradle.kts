plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.xjx.kotlin"
    compileSdk = Androids.compileSdkVersion

    defaultConfig {
        applicationId = "com.xjx.kotlin"

        minSdk = Androids.minSdkVersion
        targetSdk = Androids.targetSdkVersion
        versionCode = Androids.versionCode
        versionName = Androids.versionName

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
        getByName("release") {
            signingConfig = signingConfigs.getByName("config")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            // 给applicationId添加后缀“.debug”
            // applicationIdSuffix ".debug"
            // signingConfigs.getByName("debug")
            signingConfig = signingConfigs.getByName("test")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    // ndk version
    ndkVersion = "21.4.7075529"
}

dependencies {
    // implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    // implementation "androidx.core:core-ktx:1.2.0"

    implementation(project(":apphelper"))
    implementation(project(":apphelper2"))
    implementation(project(":http"))
    implementation(project(":refresh"))
    implementation(project(":common"))
//    implementation(project(':buildSrc'))

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.viewpager2:viewpager2:1.0.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")// 必要retrofit依赖
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0") // 必要依赖，和Rxjava结合必须用到，下面会提到
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")// 必要依赖，解析json字符所用
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")// 必要依赖，把数据转产成字符串使用

    implementation("com.github.tbruyelle:rxpermissions:0.12")
    implementation("io.reactivex.rxjava3:rxjava:3.0.4")

    //协程依赖 - 基础
    //noinspection GradleDependency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    // 协程 - activity / fragment 扩展
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    // 协程 - viewModel 扩展
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

    // 反射
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("androidx.activity:activity-ktx:1.3.0")
    implementation("androidx.fragment:fragment-ktx:1.3.0")

    implementation("org.zeromq:jeromq:0.5.3")

    //implementation(name: "Cariad_CDA_AudioRecorder_release_202302031659", ext: "aar")
    implementation(files("libs/Cariad_CDA_AudioRecorder_release_202302031659.aar"))

    implementation("com.tencent.bugly:crashreport:latest.release")
}

//def build_tools_version = "30.0.3"
//
//def sdkPath = project.android.getSdkDirectory().getAbsolutePath()
//def buildToolsPath = "${sdkPath}${File.separator}build-tools${File.separator}${build_tools_version}${File.separator}"
//def sdkAddOnPath = "${sdkPath}${File.separator}add-ons${File.separator}addon-hcp3_android_sdk_apis-harman-31${File.separator}"
//def signingKeysPath = "${sdkAddOnPath}signing${File.separator}"
//def apkSignerBatPath = buildToolsPath + "${if (org.gradle.internal.os.OperatingSystem.current().isWindows()) "apksigner.bat" else "apksigner"}"
//def keyFile = signingKeysPath + "platform.pk8"
//def certFile = signingKeysPath + "platform.x509.pem"
//def buildDir = "${project.getBuildDir()}"
//def rootDir = "${project.getRootDir()}"
//
//tasks.whenTaskAdded { task ->
//    if ("assembleDebug" == task.name || "connectedDebugAndroidTest" == task.name) {
//        def appDebugTest = new File("${buildDir}${File.separator}intermediates${File.separator}apk${File.separator}/debug/app-debug.apk")
//        if (appDebugTest.exists()) {
//            task.doFirst {
//                exec {
//                    commandLine apkSignerBatPath, "sign", "--key", keyFile, "--cert", certFile, appDebugTest.absolutePath
//                }
//            }
//        }
//    } else if (task.name.indexOf("assemble") > -1) {
//        task.doLast {
//            //always sign /outputs and /intermediates!
//            def allApks = []
//            println " rootDir ---> " + rootDir
//
//            def apkPathOutputs = new File("${buildDir}${File.separator}outputs${File.separator}apk${File.separator}")
//            if (apkPathOutputs.exists()) {
//                apkPathOutputs.eachFileRecurse(groovy.io.FileType.FILES) {
//                    if (it.name.endsWith(".apk")) {
//                        allApks << it
//                    }
//                }
//            }
//
//            if (allApks.isEmpty()) {
//                println "apks are missing/not generated. Please try again, if problem persists, give a clean build."
//                return
//            }
//
//            for (apk in allApks) {
//                println "signing $apk"
//                def signerCommand = [apkSignerBatPath, "sign", "--key", keyFile, "--cert", certFile, apk]
//                exec {
//                    commandLine signerCommand
//                }
//            }
//        }
//    }
//}


