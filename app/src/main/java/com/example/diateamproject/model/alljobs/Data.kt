package com.example.diateamproject.model.alljobs

data class Data(
    val createdAt: String,
    val jobAddress: String,
    val jobDesc: String,
    val jobId: Int,
    val jobName: String,
    val jobPosition: String,
    val jobRequirement: String,
    val jobSalary: Int,
    val jobStatus: String,
    val recruiterCompany: String,
    val recruiterDesc: String,
    val recruiterImage: String?
)