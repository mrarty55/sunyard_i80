package dev.artyboy.flutter_sunyard_i80

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.socsi.exception.SDKException
import com.socsi.smartposapi.printer.Align
import com.socsi.smartposapi.printer.FontLattice
import com.socsi.smartposapi.printer.PrintRespCode
import com.socsi.smartposapi.printer.Printer2
import com.socsi.smartposapi.printer.Printer2.PrintBuff
import com.socsi.smartposapi.printer.TextEntity

/** A module to utilize printer functionality. */
class PrinterModule {
    /** An instance of [Printer2] */
    private lateinit var printerInstance: Printer2

    /** Method string of [havePrinter]. */
    public val havePrinterMethodString: String = "havePrinter"

    /** Method string of [appendText] */
    public val appendTextMethodString: String = "appendText"

    /** Method string of [appendImage] */
    public val appendImageMethodString: String = "appendImage"

    /** Method string of [appendBarCode] */
    public val appendBarCodeMethodString: String = "appendBarCode"

    /** Method string of [appendQrCode] */
    public val appendQrCodeMethodString: String = "appendQrCode"

    /** Method string of [appendPaperFeed] */
    public val appendPaperFeedMethodString: String = "appendPaperFeed"

    /** Method string of [appendSeparatorLine] */
    public val appendSeparatorLineMethodString: String = "appendSeparatorLine"

    /** Method string of [startPrint] */
    public val startPrintMethodString: String = "startPrint"

    /** Method string of [clearPrintBuffer] */
    public val clearPrintBufferMethodString: String = "clearPrintBuffer"

    constructor(context: Context) {
        printerInstance = Printer2.getInstance(context)
    }

    /** Check if printer is available. */
    fun havePrinter(): Boolean {
        var havePrinter = false
        try {
            havePrinter = printerInstance.havePrinter()
        } catch (e: SDKException) {
            e.printStackTrace()
        }
        return havePrinter
    }

    /** Append text entity to print buffer. */
    fun appendText(text: String, isBold: Boolean, isLineBreak: Boolean, align: Align, fontSize: FontLattice): Int {
        val textEntity: TextEntity = TextEntity()
        textEntity.text = text
        textEntity.fontsize = fontSize
        textEntity.align = align
        textEntity.isBoldFont = isBold
        textEntity.isLineBreak = isLineBreak
        textEntity.fontsize = fontSize

        return printerInstance.appendTextEntity2(textEntity)
    }

    /** Append image data to print buffer. */
    fun appendImage(byteArray: ByteArray, align: Align, sampleSize: Int): Int {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize = sampleSize

        val bitmap: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, bitmapOptions)

        return printerInstance.appendImage(bitmap, align)
    }

    /** Append barcode to print buffer. */
    fun appendBarCode(data: String, pixelPoint: Int, height: Int, align: Align): Int {
        // This printer supports only 128
        return printerInstance.appendBarCodeByPixel(pixelPoint, height, align, 128, data)
    }

    /** Append single QR code to print buffer. */
    fun appendQrCode(data: String, width: Int, height: Int, leftOffset: Int): Int {
        return printerInstance.appendQrCode(width, height, leftOffset, data)
    }

    /** Append paper feed to print buffer. */
    fun appendPaperFeed(type: Int, height: Int): String {
        return printerInstance.appendTakePaper(type, height).name
    }

    /** Append a separator line to print buffer. */
    fun appendSeparatorLine(): Int {
        return printerInstance.appendTextEntity2(printerInstance.separatorLinetEntity)
    }

    /** Clear current print buffer. */
    fun clearPrintBuffer() {
        printerInstance.printBuffer.clear()
    }

    /** Start printing operation. */
    fun startPrint(): String {
        return printerInstance.startPrint().name
    }
}