plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.haridroid.realtimechatapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.haridroid.realtimechatapplication"
        minSdk = 23
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.filament.android)
    implementation(libs.play.services.gcm)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    implementation(platform(libs.firebase.bom)) // Use the BoM for version management
    implementation(libs.firebase.auth.ktx) // Add the Auth dependency
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.google.firebase.auth)
    // Also add the dependency for the Google Play services library and specify its version
    implementation(libs.play.services.auth)

    implementation(platform(libs.firebase.bom.v3223)) // Use the BoM for version management
    implementation(libs.firebase.database)


    // material lib to use bottom nevigation
    implementation(libs.material.v190)


}
apply(plugin = "com.google.gms.google-services")