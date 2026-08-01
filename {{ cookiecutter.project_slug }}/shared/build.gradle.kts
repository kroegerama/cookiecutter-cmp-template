import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.metro)
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "kotlin.uuid.ExperimentalUuidApi",
            "kotlin.time.ExperimentalTime",
            "kotlinx.coroutines.FlowPreview",
            "kotlinx.coroutines.ExperimentalCoroutinesApi",
            "androidx.compose.ui.text.ExperimentalTextApi",
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi"
        )
    }

    android {
        namespace = "{{ cookiecutter.namespace }}.shared"
        compileSdk {
            version = release(37)
        }
        minSdk {
            version = release({{ cookiecutter.min_sdk }})
        }
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
        androidResources {
            enable = true
        }
        enableCoreLibraryDesugaring = true
    }

    jvm()

//    swiftPMDependencies {
//        swiftPackage(
//            url = url("https://github.com/pmusolino/wormholy.git"),
//            version = from("2.4.0"),
//            products = listOf(product("Wormholy")),
//        )
//        iosMinimumDeploymentTarget.set("16.6")
//    }

//    iosArm64()
//    iosSimulatorArm64()
//    swiftExport {
//        moduleName = "Shared"
//        flattenPackage = "{{ cookiecutter.namespace }}"
//    }
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.arrow)
            implementation(libs.bundles.androidx.datastore)
            implementation(libs.bundles.androidx.lifecycle)
            implementation(libs.bundles.androidx.navigation3)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.kaiteki)
            implementation(libs.bundles.metro)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material3.adaptive.navigation.suite)
            implementation(libs.compose.material3.adaptive.adaptive)
            implementation(libs.compose.material3.adaptive.adaptive.layout)
            implementation(libs.compose.material3.adaptive.adaptive.navigation3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.kermit)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.kotlinx.serialization.json)

            api(projects.network)
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        iosMain.dependencies {
        }
    }
}

compose.resources {
    packageOfResClass = "{{ cookiecutter.namespace }}.resources"
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    metricsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFiles.addAll(
        layout.projectDirectory.file("compose_stability.conf")
    )
}

dependencies {
    coreLibraryDesugaring(libs.desugar)
    androidRuntimeClasspath(libs.compose.ui.tooling)
}
