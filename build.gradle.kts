buildscript {
    dependencies {
        // ... other dependencies
        classpath(libs.google.services) // Check for the latest version
    }
}
// Top-level build filewhere you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}