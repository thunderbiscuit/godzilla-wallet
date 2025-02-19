/*
 * Copyright 2020-2024 thunderbiscuit and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the ./LICENSE file.
 */

package org.bitcoindevkit.godzilla.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import org.bitcoindevkit.godzilla.presentation.theme.GodzillaColors
import java.awt.image.BufferedImage

fun generateQRCodeImage(content: String, width: Int, height: Int): BufferedImage {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height)

    val darkSquares: Int = GodzillaColors.DarkGray.toArgb()
    val background: Int = Color.White.toArgb()
    val config = MatrixToImageConfig(darkSquares, background)

    return MatrixToImageWriter.toBufferedImage(bitMatrix, config)
}
