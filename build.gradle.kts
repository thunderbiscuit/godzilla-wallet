import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.compose") version "1.7.3"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
}

group = "org.bitcoindevkit"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    mavenLocal()
    // Snapshots repository, required for org.kotlinbitcointools:bip21
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation(compose.components.resources)
    implementation(compose.desktop.currentOs)

    implementation("com.composables:composetheme:1.2.0-alpha")
    implementation("com.composables:core:1.11.2")
    implementation("com.composables:icons-lucide:1.0.0")

    // Bitcoin
    implementation("org.bitcoindevkit:bdk-jvm:1.0.0-KYOTO")
    implementation("org.kotlinbitcointools:bip21:0.0.5-SNAPSHOT")

    // QR Codes
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")
}

compose.resources {
    customDirectory(
        sourceSetName = "main",
        directoryProvider = provider { layout.projectDirectory.dir("src/main/resources") }
    )
}

compose.desktop {
    application {
        mainClass = "org.bitcoindevkit.godzilla.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Godzilla Wallet"
            packageVersion = "1.0.0"

            macOS {
                iconFile.set(project.file("Godzilla.icns"))
            }
        }
    }
}
