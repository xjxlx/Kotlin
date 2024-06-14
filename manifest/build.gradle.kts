import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.artifact.SingleArtifact

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.android.manifest"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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
}

dependencies {
//    implementation("androidx.core:core-ktx:1.10.1")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.9.0")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation(libs.espresso.core)
}

androidComponents {
    onVariants {
        val taskTaskProvider =
            project.tasks.register<TestTask>("${it.name}Manifest") {
                // 获取合并后的清单文件
                mergedManifest.set(it.artifacts.get(SingleArtifact.MERGED_MANIFEST))
            }

        it.artifacts
            .forScope(com.android.build.api.variant.ScopedArtifacts.Scope.PROJECT)
            .use(taskTaskProvider)
            .toTransform(
                ScopedArtifact.CLASSES,
                TestTask::allJars,
                TestTask::allDirectories,
                TestTask::output
            )
    }
}
