package com.example.diateamproject.model.apply

data class Data(
    val applicationId: Int,
    val applicationStatus: String,
    val createdAt: Long,
    val jobId: Int,
    val jobseekerId: Int,
    val jobseekerResume: String
)