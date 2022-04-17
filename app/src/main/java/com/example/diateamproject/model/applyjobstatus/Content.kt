package com.example.diateamproject.model.applyjobstatus

data class Content(
    val applicationStatus: String,
    val createdAt: Long,
    val jobId: Int,
    val jobName: String,
    val recruiterAddress: String,
    val recruiterCompany: String,
    val recruiterImage: Any
)