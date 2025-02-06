package org.bitcoindevkit.godzilla

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.WindowState

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Godzilla Wallet",
            resizable = false,
            state = WindowState(width = 900.dp, height = 700.dp),
        ) {
            App()
        }
    }
}
