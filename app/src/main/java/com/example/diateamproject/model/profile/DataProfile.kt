package com.example.diateamproject.model.profile

import com.example.diateamproject.model.updateprofile.SkillData

data class DataProfile(
    val jobseekerAbout: String,
    val jobseekerAddress: String,
    val jobseekerDateOfBirth: String,
    val jobseekerEducation: String,
    val jobseekerEmail: String,
    val jobseekerId: Int,
    val jobseekerImage: String,
    val jobseekerMedsos: String,
    val jobseekerName: String,
    val jobseekerPhone: String,
    val jobseekerPortfolio: String,
    val jobseekerProfession: String,
    val jobseekerResume: String,
    val skills: List<SkillData>,
    val jobsekerCompany: String,
    val workStartYear: Int,
    val workEndYear: Int
)