import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "{{ cookiecutter.namespace }}"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "{{ cookiecutter.application_id }}"
        minSdk {
            version = release({{ cookiecutter.min_sdk }})
        }
        targetSdk {
            version = release(37)
        }
        versionCode = 1
        versionName = "0.9.0"
    }

    androidResources {
        generateLocaleConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        debug {
            isDefault = true
            versionNameSuffix = "-dbg"
            applicationIdSuffix = ".dbg"
        }
        release {
            versionNameSuffix = "-rls"

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    bundle {
        language {
            enableSplit = false
        }
    }
}

base {
    archivesName = android.defaultConfig.run {
        "$applicationId-$versionName-b$versionCode"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(projects.network)

    implementation(libs.bundles.metro)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.window)
    implementation(libs.androidx.window.core)

    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.foundation)

    implementation(libs.kotlinx.coroutines.android)

    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.noop)

    coreLibraryDesugaring(libs.desugar)
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}
