package com.example.diateamproject.model.updateprofile

data class UpdateProfileResponse(
    val code: Int,
    val `data`: DataProfileUpdate,
    val status: String
)