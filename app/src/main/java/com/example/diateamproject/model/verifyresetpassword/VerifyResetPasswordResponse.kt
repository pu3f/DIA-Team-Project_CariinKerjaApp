package com.example.diateamproject.model.verifyresetpassword

data class VerifyResetPasswordResponse(
    val code: Int,
    val errorCode: String,
    val message: String,
    val status: String
)