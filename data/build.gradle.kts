import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.apollo)
    kotlin("kapt")
}

val githubGraphqlToken =
    project.findProperty(
        "GRAPHQL_TOKEN"
    ) as String? ?: ""

println("GRAPHQL_TOKEN = '$githubGraphqlToken'")
android {
    namespace = "com.karthik.pro.engr.github.api.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "GRAPHQL_TOKEN",
            "\"$githubGraphqlToken\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        create("beta") {
            initWith(getByName("release"))
            matchingFallbacks += listOf("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    room {
        schemaDirectory(
            "$projectDir/schemas"
        )
    }

    apollo {
        service("github") {
            packageName.set(
                "com.karthik.pro.engr.github.api.data.graphql"
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation (libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    // Allow @Inject (javax) without bringing Hilt runtime here
    implementation(libs.javax.inject)

    implementation(project(":domain"))
    implementation(project(":core"))
    implementation(project(":core-testing"))

    implementation(libs.paging)

    implementation(libs.hilt.android)
    implementation(libs.androidx.paging.runtime.ktx)
    kapt(libs.hilt.compiler)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.apollo.runtime)


    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.okhttp.mockwebserver)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(project(":core-testing"))



}
