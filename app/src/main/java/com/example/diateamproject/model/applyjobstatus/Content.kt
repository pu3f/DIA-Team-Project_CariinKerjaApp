package com.example.diateamproject.model.applyjobstatus

data class Content(
    val applicationStatus: String,
    val createdAt: String,
    val jobId: Int,
    val jobName: String,
    val recruiterAddress: String,
    val recruiterCompany: String,
    val recruiterImage: String
)