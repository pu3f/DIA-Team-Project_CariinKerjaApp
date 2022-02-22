package com.example.diateamproject.model.getalljob

data class JobResponse(
    val code: Int,
    val `data`: List<Data>,
    val status: String
)