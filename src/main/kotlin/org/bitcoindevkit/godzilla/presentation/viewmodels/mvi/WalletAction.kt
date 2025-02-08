package org.bitcoindevkit.godzilla.presentation.viewmodels.mvi

sealed interface WalletAction {
    data object StartKyoto : WalletAction
    data class CreateSale(val description: String, val amount: ULong) : WalletAction
    data object BottomSheetClosed : WalletAction
}
