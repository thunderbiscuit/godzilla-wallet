package org.bitcoindevkit.godzilla

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.WindowState
import com.composables.composetheme.buildComposeTheme
import com.composables.core.rememberDialogState
import org.bitcoindevkit.godzilla.presentation.ui.App
import org.bitcoindevkit.godzilla.presentation.ui.NewSaleDialog
import org.bitcoindevkit.godzilla.presentation.viewmodels.MainViewModel
import org.bitcoindevkit.godzilla.presentation.viewmodels.mvi.WalletAction

val GodzillaTheme = buildComposeTheme {  }

/*
 * The entry point for the app. This variant does not have any navigation component, and simply uses a dialog sitting
 * on top of the main screen as a sort of secondary screen to produce the specific sale data. Note that the core UI is
 * built using 2 root composables only:
 *   GodzillaTheme {
 *       App()
 *       NewSaleDialog()
 *   }
 */
fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Godzilla Wallet",
            resizable = false,
            state = WindowState(width = 900.dp, height = 700.dp),
        ) {
            // This variable holds the state of the dialog (visible or hidden)
            val dialogState = rememberDialogState()

            // We instantiate the single viewmodel for the whole app here, ensuring it outlives all composables.
            val mainViewModel = MainViewModel(dialogState)

            MenuBar {
                Menu("Action", mnemonic = 'A') {
                    Item("New Sale",  onClick = {}, mnemonic = 'N')
                    Item(
                        text = "Start Compact Block Filter Node",
                        onClick = { mainViewModel.onAction(WalletAction.StartKyoto) },
                        mnemonic = 'K'
                    )
                }
            }

            GodzillaTheme {
                // The core MVI pattern is to pass down to composables a state data structure containing what the UI
                // needs to render correctly (here a WalletState object) as well as an onAction() method which the UI
                // components can trigger given certain events.
                App(
                    walletState = mainViewModel.walletState.value,
                    onAction = mainViewModel::onAction
                )

                NewSaleDialog(
                    dialogState = dialogState,
                    closeDialog = { dialogState.visible = false },
                    walletState = mainViewModel.walletState.value,
                    onAction = mainViewModel::onAction
                )
            }
        }
    }
}
