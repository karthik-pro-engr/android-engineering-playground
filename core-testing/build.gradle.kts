plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {

    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.junit)

    implementation(project(":domain"))
    implementation(libs.kotlinx.coroutines.core)
    

    testImplementation(libs.junit)

}