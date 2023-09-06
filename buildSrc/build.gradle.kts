//plugins {
//    `kotlin-dsl`
//}
//
//repositories {
//    // The org.jetbrains.kotlin.jvm plugin requires a repository
//    // where to download the Kotlin compiler dependencies from.
//    mavenCentral()
//}
//
//dependencies {
//    implementation("com.android.tools.build:gradle-api:8.0.2")
//    implementation(kotlin("stdlib", "1.8.10"))
//    gradleApi()
//}


plugins {
    `kotlin-dsl`
//    kotlin("jvm") version "1.8.0"
}
repositories {
    google()
    mavenCentral()
}
dependencies {
//    implementation("com.android.tools.build:gradle-api:8.0.2")
//    implementation(kotlin("stdlib", "1.8.10"))
    gradleApi()
}