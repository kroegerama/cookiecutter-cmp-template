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
        mainClass = "com.jetbrains.kmpapp.MainKt"

//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "com.jetbrains.kmpapp"
//            packageVersion = "1.0.0"
//        }

        buildTypes.release.proguard {
            isEnabled = false
        }
    }
}

compose.resources {
    packageOfResClass = "com.jetbrains.kmpapp.resources"
}
