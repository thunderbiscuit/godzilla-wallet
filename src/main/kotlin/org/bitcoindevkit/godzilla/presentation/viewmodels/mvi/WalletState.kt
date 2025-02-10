package org.bitcoindevkit.godzilla.presentation.viewmodels.mvi

import java.awt.image.BufferedImage

data class WalletState(
    val kyotoReady: Boolean,
    val closeBottomSheet: Boolean,
    val newSale: Triple<String, ULong, BufferedImage>? = null,
    val paymentCompleted: Boolean = false
)
