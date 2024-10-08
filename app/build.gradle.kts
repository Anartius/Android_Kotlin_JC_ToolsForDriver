plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.toolsfordriver"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.toolsfordriver"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType(type = org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask::class) {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
    implementation("com.google.firebase:firebase-firestore:25.1.0")

    // Compose
    val composeBomVersion = "2024.09.01"
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.1")

    // Lifecycle Scope
    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")

    // Coroutines
    val coroutinesVersion = "1.8.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion")

    // Dagger - Hilt
    val daggerHiltVersion = "2.52"
    val androidHiltVersion = "1.2.0"
    implementation("com.google.dagger:hilt-android:$daggerHiltVersion")
    ksp("com.google.dagger:hilt-compiler:$daggerHiltVersion")
    ksp("androidx.hilt:hilt-compiler:$androidHiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:$androidHiltVersion")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    testImplementation("androidx.room:room-testing:$roomVersion")

    // Firebase/firestore
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

    // DateTime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Camera X
    val cameraVersion = "1.3.4"
    implementation("androidx.camera:camera-core:$cameraVersion")
    implementation("androidx.camera:camera-core:$cameraVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraVersion")
    implementation("androidx.camera:camera-view:$cameraVersion")
    implementation("androidx.camera:camera-extensions:$cameraVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}