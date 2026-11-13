package com.project.qrvault.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QRCodeUtils {

    fun generateVCardData(name: String, phone: String, email: String): String {
        val sb = StringBuilder()
        sb.append("BEGIN:VCARD\n")
        sb.append("VERSION:3.0\n")
        sb.append("FN:").append(name).append("\n")
        if (phone.isNotBlank()) {
            sb.append("TEL:").append(phone).append("\n")
        }
        if (email.isNotBlank()) {
            sb.append("EMAIL:").append(email).append("\n")
        }
        sb.append("END:VCARD")
        return sb.toString()
    }

    fun generateQRCodeBitmap(
        content: String,
        width: Int = 512,
        height: Int = 512,
        qrColor: Int = Color.BLACK,
        bgColor: Int = Color.WHITE
    ): Bitmap? {
        if (content.isEmpty()) return null

        val hints = HashMap<EncodeHintType, Any>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.MARGIN] = 1

        return try {
            val bitMatrix = MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints
            )
            val matrixWidth = bitMatrix.width
            val matrixHeight = bitMatrix.height
            val pixels = IntArray(matrixWidth * matrixHeight)

            for (y in 0 until matrixHeight) {
                val offset = y * matrixWidth
                for (x in 0 until matrixWidth) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) qrColor else bgColor
                }
            }

            val bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight)
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}
