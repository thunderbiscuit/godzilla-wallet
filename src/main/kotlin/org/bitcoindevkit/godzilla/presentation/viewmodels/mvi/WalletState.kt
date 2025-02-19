/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package org.bitcoindevkit.godzilla.presentation.viewmodels.mvi

import java.awt.image.BufferedImage

data class WalletState(
    val kyotoReady: Boolean = false,
    val closeBottomSheet: Boolean = false,
    val newSale: NewSale? = null,
    val paymentCompleted: Boolean = false
)

data class NewSale(
    val description: String,
    val amount: Long,
    val qrCode: BufferedImage
)
