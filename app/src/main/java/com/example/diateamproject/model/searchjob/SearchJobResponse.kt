package com.example.diateamproject.model.searchjob

data class SearchJobResponse(
    val code: Int,
    val `data`: List<Jobs>,
    val status: String
)