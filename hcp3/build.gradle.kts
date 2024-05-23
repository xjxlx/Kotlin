plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
//    val sdkPath = project.android.sdkDirectory.absolutePath
//    val addOnPath = "$sdkPath/add-ons/addon-hcp3_android_sdk_apis-harman-33/libs"
//    compileOnly(fileTree(mapOf("dir" to addOnPath, "include" to listOf("android.car.jar"))))

    implementation(fileTree(mapOf("dir" to "libs/cluster46_12_7_0", "include" to listOf("*.jar"))))
    implementation(fileTree(mapOf("dir" to "libs/oia", "include" to listOf("*.jar"))))
    // 用于生成代码的依赖
    implementation("com.squareup:javapoet:1.13.0")
    implementation("org.projectlombok:lombok:1.18.30")
    implementation("androidx.annotation:annotation-jvm:1.8.0")
}
