package org.bitcoindevkit.godzilla.domain

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import java.awt.image.BufferedImage

fun generateQRCodeImage(content: String, width: Int, height: Int): BufferedImage {
    val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height)

    val darkSquares: Int = 0xFF212121.toInt()
    val background: Int = 0xFFffffff.toInt()
    val config = MatrixToImageConfig(darkSquares, background)

    return MatrixToImageWriter.toBufferedImage(bitMatrix, config)
}
