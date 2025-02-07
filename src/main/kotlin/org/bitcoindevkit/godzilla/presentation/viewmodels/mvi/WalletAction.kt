package org.bitcoindevkit.godzilla.presentation.viewmodels.mvi

sealed interface WalletAction {
    data object StartKyoto : WalletAction
}
