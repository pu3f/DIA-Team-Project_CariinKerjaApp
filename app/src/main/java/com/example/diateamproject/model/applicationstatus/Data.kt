package com.example.diateamproject.model.applicationstatus

data class Data(
    val jobId: Int,
    val jobName: String,
    val recruiterCompany: String,
    val recruiterAddress: String,
    val recruiterImage: String?,
    val applicationStatus: String,
    val createdAt: Long
)