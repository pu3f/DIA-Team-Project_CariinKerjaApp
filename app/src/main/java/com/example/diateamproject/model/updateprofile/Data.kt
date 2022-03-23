package com.example.diateamproject.model.updateprofile

data class Data(
    val jobseekerAbout: String,
    val jobseekerAddress: String,
    val jobseekerDateOfBirth: Long,
    val jobseekerEducation: String,
    val jobseekerEmail: String,
    val jobseekerId: Int,
    val jobseekerImage: String?,
    val jobseekerName: String,
    val jobseekerPhone: String,
    val jobseekerResume: String
)