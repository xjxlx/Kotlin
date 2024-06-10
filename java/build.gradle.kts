plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation("org.zeromq:jeromq:0.5.3")
    implementation(kotlin("stdlib-jdk8"))
}
