package com.example.diateamproject.model.emailresetpassword

data class EmailResetPasswordResponse(
    val code: Int,
    val message: String,
    val status: String
)