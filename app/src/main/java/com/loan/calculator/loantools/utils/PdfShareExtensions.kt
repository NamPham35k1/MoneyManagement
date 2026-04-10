package com.loan.calculator.loantools.utils


import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

/**
 * Tạo File PDF từ một View
 */
fun View.createPdf(fileName: String): File? {
    val document = PdfDocument()

    // Thiết lập kích thước trang dựa trên kích thước thực tế của View
    val pageInfo = PdfDocument.PageInfo.Builder(this.width, this.height, 1).create()
    val page = document.startPage(pageInfo)

    // Vẽ nội dung View lên trang PDF
    val canvas: Canvas = page.canvas
    this.draw(canvas)

    document.finishPage(page)

    // Lưu vào thư mục cache của ứng dụng
    val outputFile = File(context.cacheDir, "$fileName.pdf")
    return try {
        val outputStream = FileOutputStream(outputFile)
        document.writeTo(outputStream)
        outputStream.close()
        document.close()
        outputFile
    } catch (e: Exception) {
        e.printStackTrace()
        document.close()
        null
    }
}

/**
 * Chia sẻ File PDF thông qua FileProvider
 */
fun Context.sharePdf(file: File) {
    val uri = FileProvider.getUriForFile(
        this,
        "${packageName}.fileprovider",
        file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    startActivity(Intent.createChooser(intent, "Share Result PDF"))
}