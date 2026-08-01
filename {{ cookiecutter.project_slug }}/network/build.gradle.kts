import de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.time.OffsetDateTime

plugins {
    alias(libs.plugins.android.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kmpgen)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.metro)
}

kotlin {
    android {
        namespace = "{{ cookiecutter.namespace }}.api"
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

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.metro)
            implementation(libs.bundles.androidx.datastore)

            api(libs.kaiteki.core)
            implementation(libs.kermit)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kmpgen.companion)
        }
        androidMain.dependencies {
        }
        iosMain.dependencies {
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar)
    androidRuntimeClasspath(libs.compose.ui.tooling)
}

val specInfos = listOf(
    Triple("{{ cookiecutter.namespace }}.api.pokeapi", "https://raw.githubusercontent.com/PokeAPI/pokeapi/refs/heads/master/openapi.yml", "pokeapi.yaml"),
    Triple("{{ cookiecutter.namespace }}.api.neows", "https://api.apis.guru/v2/specs/neowsapp.com/1.0/openapi.json", "neows.json"),
)

kmpgen {
    createdAt = OffsetDateTime.parse("2026-06-01T13:00:00Z")

    specInfos.forEach { (pkg, _, file) ->
        spec(
            packageName = pkg
        ) {
            specFile = file(file)
        }
    }
}

val downloadTasks = specInfos.map { (_, url, file) ->
    tasks.register<Download>("downloadSpec_$file") {
        group = "kmpgen"
        description = "Download the latest OpenAPI spec"
        src(url)
        dest(file)
        overwrite(true)
    }
}.toTypedArray()

tasks.register("downloadSpecs") {
    group = "kmpgen"
    description = "Download the latest OpenAPI specs"
    dependsOn(*downloadTasks)
}
