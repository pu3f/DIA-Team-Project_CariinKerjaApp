package com.example.diateamproject.model.profile

data class ProfileResponse(
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