package com.example.diateamproject.model.allpostingjobs

data class Content(
    val createdAt: String,
    val jobAddress: String,
    val jobId: Int,
    val jobName: String,
    val jobPosition: String,
    val jobSalary: Int,
    val jobStatus: String,
    val recruiterCompany: String,
    val recruiterImage: String
)