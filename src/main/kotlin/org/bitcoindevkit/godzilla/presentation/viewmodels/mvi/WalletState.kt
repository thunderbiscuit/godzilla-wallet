package org.bitcoindevkit.godzilla.presentation.viewmodels.mvi

data class WalletState(
    val kyotoReady: Boolean,
    val closeBottomSheet: Boolean,
    val newSale: Pair<String, ULong>? = null
)
