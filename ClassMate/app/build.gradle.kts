plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.hype.classmate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hype.classmate"
        minSdk = 24
        targetSdk = 33
        versionCode = 4
        versionName = "1.1"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.legacy:legacy-support-v13:1.0.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")

    implementation("com.github.yukuku:ambilwarna:2.0.1")
    // Calendar
    // https://github.com/islandparadise14/MinTimetable
    implementation("com.github.islandparadise14:Mintable:1.5.1")
    // https://github.com/thellmund/Android-Week-View
    implementation("com.github.thellmund.Android-Week-View:core:5.3.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}