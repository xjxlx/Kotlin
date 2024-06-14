plugins {
    kotlin("jvm") version "1.9.0"
}
repositories {
    google()
    mavenCentral()
}
kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation("com.android.tools.build:gradle-api:8.1.1")
    implementation(kotlin("stdlib", "1.9.0"))
    gradleApi()
}
