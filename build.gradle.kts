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
}

dependencies {
    implementation(compose.components.resources)
    implementation(compose.desktop.currentOs)
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
