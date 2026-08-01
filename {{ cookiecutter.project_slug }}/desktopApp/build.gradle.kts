plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose)
}

kotlin {

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
//            packageVersion = "1.0.0"
//        }

        buildTypes.release.proguard {
            isEnabled = false
        }
    }
}

compose.resources {
    packageOfResClass = "{{ cookiecutter.namespace }}.resources"
}
