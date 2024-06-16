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
    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation(libs.espresso.core)
}

// androidComponents {
//    beforeVariants { variants ->
//        println("--------------------------------beforeVariants ")
//    }
//
//    onVariants {
//        println("manifest ---------------->")
//        val taskName = "${it.name}Manifest"
//        val taskTaskProvider =
//            project.tasks.register<TestTask>(taskName) {
//                // 获取合并后的清单文件
//                mergedManifest.set(it.artifacts.get(SingleArtifact.MERGED_MANIFEST))
//                // dependsOn(tasks.findByName("assembleDebug"))
//            }
//
//        it.artifacts
//            .forScope(com.android.build.api.variant.ScopedArtifacts.Scope.PROJECT)
//            .use(taskTaskProvider)
//            .toTransform(
//                ScopedArtifact.CLASSES,
//                TestTask::allJars,
//                TestTask::allDirectories,
//                TestTask::output
//            )
//    }
// }

// tasks.register<TestTask>("addPermissionToManifest") {
//    group = "custom"
//    description = "Adds permission to AndroidManifest.xml"
// }

tasks.whenTaskAdded {
//    println("Task - manifest - added: [${this.name}]")
//    if (this.name == "assembleDebug") {
//        val debugTask = tasks.findByName("addPermissionToManifest")
//        println("debugTask:$debugTask")
//        if (debugTask != null) {
//            println("assembleDebug-------------")
//            this.dependsOn(debugTask)
//        }
//    }
}

abstract class ManifestTransformerTask : DefaultTask() {
    @get:InputFile
    abstract val mergedManifest: RegularFileProperty

    @get:OutputFile
    abstract val updatedManifest: RegularFileProperty

    @TaskAction
    fun taskAction() {
        val path = mergedManifest.asFile.get().path
        println.red("mergedManifest-path: $path")

        var manifest = mergedManifest.asFile.get().readText()
        println.red("manifest: $manifest")
        val readLine = "</manifest>"
        manifest =
            manifest.replace(
                readLine,
                "<uses-permission android:name=\"android.permission.custom\"/>\n$readLine"
            )
        updatedManifest.get().asFile.writeText(manifest)
        println.red("Writes to " + updatedManifest.get().asFile.absolutePath)
    }
}

androidComponents {
    onVariants { variant ->
//        val manifestUpdater =
//            tasks.register<ManifestTransformerTask>("${variant.name}Manifest") {
//                mergedManifest.set(variant.artifacts.get(SingleArtifact.MERGED_MANIFEST))
//            }
//
//        variant.artifacts
//            .use(manifestUpdater)
//            .wiredWithFiles(
//                ManifestTransformerTask::mergedManifest,
//                ManifestTransformerTask::updatedManifest
//            ).toTransform(SingleArtifact.MERGED_MANIFEST)

        val taskProvider =
            project.tasks.register<ModifyClassesTask>("${variant.name}ModifyAllClasses") {
                mergedManifest.set(variant.artifacts.get(SingleArtifact.MERGED_MANIFEST))
            }
        variant.artifacts
            .forScope(com.android.build.api.variant.ScopedArtifacts.Scope.PROJECT)
            .use(taskProvider)
            .toTransform(
                ScopedArtifact.CLASSES,
                ModifyClassesTask::allJars,
                ModifyClassesTask::allDirectories,
                ModifyClassesTask::output
            )
    }
}
