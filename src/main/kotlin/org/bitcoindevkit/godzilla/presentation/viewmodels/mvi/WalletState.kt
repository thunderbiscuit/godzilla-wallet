package org.bitcoindevkit.godzilla.presentation.viewmodels.mvi

import java.awt.image.BufferedImage

data class WalletState(
    val kyotoReady: Boolean,
    val closeBottomSheet: Boolean,
    val newSale: NewSale? = null,
    val paymentCompleted: Boolean = false
)

data class NewSale(
    val description: String,
    val amount: Long,
    val qrCode: BufferedImage
)
