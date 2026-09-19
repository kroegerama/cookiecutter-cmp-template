plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
}

val appVersionName = providers.gradleProperty("app.versionName")
val appVersionCode = providers.gradleProperty("app.versionCode")

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

val appVersionDir = layout.buildDirectory.dir("generated/appVersion")
val generateAppVersion = tasks.register<WriteProperties>("generateAppVersion") {
    description = "Writes the app version into a classpath resource."
    destinationFile = appVersionDir.map { it.file("app-version.properties") }
    property("versionName", appVersionName)
    property("versionCode", appVersionCode)
    property("applicationId", "{{ cookiecutter.application_id }}")
}

sourceSets.main {
    resources.srcDir(files(appVersionDir).builtBy(generateAppVersion))
}

dependencies {
    implementation(projects.shared)

    implementation(libs.kotlinx.coroutines.swing)
    implementation(libs.slf4j.simple)
    implementation(libs.compose.components.resources)

    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "{{ cookiecutter.namespace }}.MainKt"

//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "{{ cookiecutter.namespace }}"
//            packageVersion = appVersionName.get()
//        }

        buildTypes.release.proguard {
            isEnabled = false
        }
    }
}

compose.resources {
    packageOfResClass = "{{ cookiecutter.namespace }}.resources"
}
