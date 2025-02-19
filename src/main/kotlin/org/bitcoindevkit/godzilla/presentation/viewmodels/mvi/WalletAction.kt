/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package org.bitcoindevkit.godzilla.presentation.viewmodels.mvi

sealed interface WalletAction {
    data object StartKyoto : WalletAction
    data class  CreateSale(val description: String, val amount: Long) : WalletAction
    data object BottomSheetClosed : WalletAction
    data object OpenDialog : WalletAction
    data object DismissDialog : WalletAction
    data object ReadyForNewPayment : WalletAction
}
