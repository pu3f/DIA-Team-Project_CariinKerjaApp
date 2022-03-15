package com.example.diateamproject.listener

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

interface ConvertToDate {
    @RequiresApi(Build.VERSION_CODES.O)
    fun main(args: Array<String>) {

        @SuppressLint("SimpleDateFormat")
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

        val milliseconds: Long = 1000000
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val formatted = current.format(formatter)


        // long minutes = (milliseconds / 1000) / 60;
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)

        // long seconds = (milliseconds / 1000);
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
    }
}