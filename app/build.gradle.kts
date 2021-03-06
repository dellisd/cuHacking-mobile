import org.jetbrains.kotlin.config.KotlinCompilerVersion
import java.util.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("io.fabric")
    id("com.squareup.sqldelight")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(ProjectVersions.COMPILE_SDK)
    defaultConfig {
        applicationId = "com.cuhacking.app"
        minSdkVersion(ProjectVersions.MIN_SDK)
        targetSdkVersion(ProjectVersions.TARGET_SDK)
        versionCode = "git rev-list --first-parent --count HEAD".execute().text.trim().toInt()
        versionName = "git describe --tag".execute().text.trim()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val props = Properties()
        val localPropsFile = project.rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            props.load(localPropsFile.inputStream())
        }

        if (props.containsKey("api.endpoint") && props.containsKey("mapbox.key")) {
            buildConfigField("String", "API_ENDPOINT", "\"${props.getProperty("api.endpoint")}\"")
            buildConfigField("String", "MAPBOX_KEY", "\"${props.getProperty("mapbox.key")}\"")
        } else {
            throw GradleException("mapbox.key and api.endpoint not declared in local.properties")
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
    }

    kotlinOptions {
        this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        jvmTarget = "1.8"
    }
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    implementation("androidx.appcompat:appcompat:${ProjectVersions.APPCOMPAT}")
    implementation("androidx.core:core-ktx:${ProjectVersions.KTX}")
    implementation("androidx.constraintlayout:constraintlayout:${ProjectVersions.CONSTRAINT_LAYOUT}")
    implementation("com.google.android.material:material:${ProjectVersions.MATERIAL}")
    implementation("androidx.fragment:fragment-ktx:${ProjectVersions.FRAGMENT}")
    implementation("androidx.lifecycle:lifecycle-extensions:${ProjectVersions.LIFECYCLE}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${ProjectVersions.LIFECYCLE}")
    implementation("androidx.navigation:navigation-fragment-ktx:${ProjectVersions.NAVIGATION}")
    implementation("androidx.navigation:navigation-ui-ktx:${ProjectVersions.NAVIGATION}")
    implementation("androidx.camera:camera-core:${ProjectVersions.CAMERA}")
    implementation("androidx.camera:camera-camera2:${ProjectVersions.CAMERA}")
    implementation("androidx.camera:camera-view:${ProjectVersions.CAMERA_VIEW}")
    implementation("androidx.camera:camera-extensions:${ProjectVersions.CAMERA_EXTENSIONS}")
    implementation("androidx.camera:camera-lifecycle:${ProjectVersions.CAMERA_LIFECYCLE}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${ProjectVersions.SWIPE_REFRESH}")

    implementation("com.google.dagger:dagger:${ProjectVersions.DAGGER}")
    kapt("com.google.dagger:dagger-compiler:${ProjectVersions.DAGGER}")
    kapt("com.google.dagger:dagger-android-processor:${ProjectVersions.DAGGER}")

    implementation("com.github.bumptech.glide:glide:${ProjectVersions.GLIDE}")
    kapt("com.github.bumptech.glide:compiler:${ProjectVersions.GLIDE}")

    implementation("com.google.firebase:firebase-analytics:${ProjectVersions.FIREBASE_CORE}")
    implementation("com.google.firebase:firebase-auth:${ProjectVersions.FIREBASE_AUTH}")
    implementation("com.google.firebase:firebase-messaging:${ProjectVersions.FIREBASE_CLOUD_MESSAGING}")
    implementation("com.google.firebase:firebase-ml-vision:${ProjectVersions.FIREBASE_VISION}")
    implementation("com.google.firebase:firebase-ml-vision-barcode-model:${ProjectVersions.FIREBASE_VISION_BARCODE_MODEL}")
    implementation("com.crashlytics.sdk.android:crashlytics:${ProjectVersions.FIREBASE_CRASHLYTICS}")

    implementation("com.squareup.sqldelight:android-driver:${ProjectVersions.SQLDELIGHT}")
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:${ProjectVersions.SQLDELIGHT}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${ProjectVersions.COROUTINES}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${ProjectVersions.COROUTINES}")
    implementation("com.squareup.retrofit2:retrofit:${ProjectVersions.RETROFIT}")
    implementation("com.squareup.retrofit2:converter-moshi:${ProjectVersions.RETROFIT}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${ProjectVersions.RETROFIT_SERIALIZATION}")
    implementation("com.squareup.okhttp3:okhttp:${ProjectVersions.OKHTTP}")
    implementation("com.squareup.okhttp3:logging-interceptor:${ProjectVersions.OKHTTP}")
    implementation("com.mapbox.mapboxsdk:mapbox-android-sdk:${ProjectVersions.MAPBOX}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${ProjectVersions.SERIALIZATION}")
    implementation("com.github.kenglxn.QRGen:android:${ProjectVersions.QRGEN}")
    implementation("com.jakewharton.threetenabp:threetenabp:${ProjectVersions.THREE_TEN_ABP}")
    implementation("uk.co.samuelwall:material-tap-target-prompt:${ProjectVersions.MATERIAL_TAP_TARGET_PROMPT}")
    implementation("com.github.zagum:Android-ExpandIcon:${ProjectVersions.ANDROID_EXPAND_ICON}")

    testImplementation("junit:junit:${ProjectVersions.JUNIT}")
    testImplementation("androidx.arch.core:core-testing:${ProjectVersions.LIFECYCLE}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${ProjectVersions.COROUTINES}")
    testImplementation("com.squareup.sqldelight:sqlite-driver:${ProjectVersions.SQLDELIGHT}")
    testImplementation("com.squareup.okhttp3:mockwebserver:${ProjectVersions.OKHTTP}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${ProjectVersions.ESPRESSO_CORE}")
    androidTestImplementation("androidx.test:rules:${ProjectVersions.TEST_RULES}")
    androidTestImplementation("androidx.test.ext:junit:${ProjectVersions.TEST_JUNIT}")

    implementation(project(":common"))
}

apply(plugin = "com.google.gms.google-services")
