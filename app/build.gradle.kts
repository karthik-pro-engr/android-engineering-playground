import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
import java.util.Base64

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.firebase.appdistribution)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("kapt")

}

android {
    namespace = "com.karthik.pro.engr.github.api.playground"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.karthik.pro.engr.github.api.playground"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("release.jks")
            storePassword = System.getenv("RELEASE_STORE_PASSWORD")
            keyAlias = System.getenv("RELEASE_KEY_ALIAS")
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("boolean", "ENABLE_APP_DISTRIBUTION", "false")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "ENABLE_APP_DISTRIBUTION", "false")
        }
        create("beta") {
            // Start from release so beta is signed the same and uses release-like settings
            initWith(getByName("release"))
            buildConfigField("boolean", "ENABLE_APP_DISTRIBUTION", "true")

            // ensure beta is signed with release keystore so it's valid for App Distribution
            signingConfig = signingConfigs.getByName("release")

            // if you want fast iteration, you can disable minify for beta:
            isMinifyEnabled = false

            // fallback to release resources/configs if some plugin expects release
//            matchingFallbacks += listOf("release")
            versionNameSuffix = "-beta"
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
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

firebaseAppDistribution {
    // required: your app id from Firebase console (Android)
    appId = "1:757583569857:android:274f6b3c2d44d03a0f4540"

    // 🔍 Debug print to confirm resolved path
    println(
        "Resolved Firebase credentials path:" +
                " ${file("firebase-service-account.json").absolutePath}"
    )

    // service account json path (the workflow writes file to repo root)
    serviceCredentialsFile = file("firebase-service-account.json").absolutePath

    // who to notify. Either group(s) defined in Firebase console:
    groups = "family" // example group name(s), comma-separated if multiple

    // Or specify testers directly (comma-separated emails)
    // testers = "qa1@example.com,qa2@example.com"

    // optional release notes: you can pass via -PreleaseNotes in workflow
    // releaseNotes = "Automated CI release $VERSION_NAME"
}

tasks.register("prepareFirebaseCredentials") {
    doFirst {
        val base64Credentials = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON")
        val file = File("${project.projectDir}/firebase-service-account.json")
        file.writeText(base64Credentials ?: error("Missing FIREBASE_SERVICE_ACCOUNT_JSON"))
        if (base64Credentials.isNullOrBlank()) {
            error("❌ GITHUB_FIREBASE_SERVICE_ACCOUNT_JSON is missing or empty")
        }

        val decoded = Base64.getDecoder().decode(base64Credentials)
        file.writeBytes(decoded)

        println("✅ Firebase credentials written to ${file.absolutePath}")
    }
}

tasks.register("prepareReleaseKeystore") {
    doFirst {
        val base64Keystore = System.getenv("KEYSTORE_BASE64")
        val file = File("${project.projectDir}/release.jks")

        println("🔍 Preparing release.jks for signing...")
        println("🔍 Target path: ${file.absolutePath}")

        if (base64Keystore.isNullOrBlank()) {
            error("❌ KEYSTORE_BASE64 is missing or empty")
        }

        val decoded = Base64.getDecoder().decode(base64Keystore)
        file.writeBytes(decoded)

        println("✅ release.jks written successfully")
    }
}

fun DependencyHandler.betaImplementation(dependencyNotation: Any) {
    add("betaImplementation", dependencyNotation)
}


dependencies {

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    implementation(libs.hilt.android)
    implementation(libs.androidx.paging.runtime.ktx)
    kapt(libs.hilt.compiler)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.compose.ui)
    implementation(libs.compose.material)


    implementation(libs.hilt.navigation.compose)

    implementation(libs.all.variants.preview)

    implementation(libs.firebase.feedback.api)
    betaImplementation(libs.karthik.pro.engr.firebase.feedback.impl)

    implementation(libs.paging)


    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(project(":core-testing"))

}
kapt {
    correctErrorTypes = true
}