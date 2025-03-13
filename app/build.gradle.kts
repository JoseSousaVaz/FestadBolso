plugins {
    alias(libs.plugins.android.application)
    // Apply the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.festadbolso"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.festadbolso"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))

    // Firebase dependencies
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)

    // ✅ Add this for FirebaseUI Authentication
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")

    //Glide image user
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    //Use apis
    implementation("com.android.volley:volley:1.2.1")
    implementation("org.json:json:20210307")

    
}
