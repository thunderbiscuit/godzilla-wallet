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

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Godzilla Wallet",
            resizable = false,
            state = WindowState(width = 900.dp, height = 700.dp),
        ) {
            val dialogState = rememberDialogState()
            val mainViewModel: MainViewModel = MainViewModel(dialogState)

            MenuBar {
                Menu("Action", mnemonic = 'A') {
                    Item("New Sale",  onClick = {}, mnemonic = 'N')
                    Item(
                        text = "Compact Block Filter Node",
                        onClick = { mainViewModel.onAction(WalletAction.StartKyoto) },
                        mnemonic = 'K'
                    )
                }
            }

            GodzillaTheme {
                App(mainViewModel, mainViewModel.walletState.value)

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
