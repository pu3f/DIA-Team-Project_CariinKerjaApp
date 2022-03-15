package com.example.diateamproject.model.applicationstatus

data class Data(
    val applicationStatus: String,
    val createdAt: Long,
    val jobId: Int,
    val jobName: String,
    val recruiterAddress: String,
    val recruiterCompany: String
)