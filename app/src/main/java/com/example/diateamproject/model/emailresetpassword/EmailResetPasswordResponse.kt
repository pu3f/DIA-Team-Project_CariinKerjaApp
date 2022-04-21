package com.example.diateamproject.model.emailresetpassword

data class EmailResetPasswordResponse(
    val code: Int,
    val data: String,
    val status: String
)