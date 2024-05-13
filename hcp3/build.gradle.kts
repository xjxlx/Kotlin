plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // 用于生成代码的依赖
    implementation("com.squareup:javapoet:1.13.0")
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("org.projectlombok:lombok:1.18.30")
}
