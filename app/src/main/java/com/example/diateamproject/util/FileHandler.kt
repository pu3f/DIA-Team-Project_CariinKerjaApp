package com.example.diateamproject.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FileHandler {
    fun handleUri(context: Context, uri: Uri): String? {
        context.apply {
            val type = when (contentResolver.getType(uri)) {
                "image/jpeg" -> ".jpeg"
                //another types
                "application/pdf" -> ".pdf"
                else -> return null
            }
            val dir = File(cacheDir, "dir_name").apply { mkdir() }
            val path = copyStreamToFile(
                contentResolver.openInputStream(uri)!!,
                File(dir, "${System.currentTimeMillis()}$type")
            )
            return path
        }

    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File): String? {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }

        return outputFile.absolutePath
    }
}