package com.example.diateamproject.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

@SuppressLint("Range")
//extension function get file name from selected image Uri
fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val cursor = this.query(fileUri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}
