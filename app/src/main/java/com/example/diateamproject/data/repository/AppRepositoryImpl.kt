package com.example.diateamproject.data.repository

import com.example.diateamproject.data.remote.AppRemoteDataSource
import com.example.diateamproject.model.alljobs.AllJobsResponse
import com.example.diateamproject.model.applicationstatus.ApplicationStatusResponse
import com.example.diateamproject.model.apply.ApplyResponse
import com.example.diateamproject.model.jobbyid.JobByIdResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.profile.ProfileResponse
import com.example.diateamproject.model.updateprofile.UpdateProfileResponse
import com.example.diateamproject.model.register.RegisterResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(private val remoteDataSource: AppRemoteDataSource) :
    AppRepository {

    override fun getAllJobs(): Single<AllJobsResponse> {
        return remoteDataSource.getAllJobs()
    }

    override fun getRecentJobs(): Single<AllJobsResponse> {
        return remoteDataSource.getRecentJobs()
    }

    override fun getApplicationStatus(id: Int?): Single<ApplicationStatusResponse> {
        return remoteDataSource.getApplicationStatus(id)
    }

    override fun getProfile(id: Int?): Single<ProfileResponse> {
        return remoteDataSource.getProfile(id)
    }

    override fun getJobById(id: Int?): Single<JobByIdResponse> {
        return remoteDataSource.getJobById(id)
    }

    override fun postLogin(
        jobseekerEmail: String?,
        jobseekerPassword: String?
    ): Single<LoginResponse> {
        return remoteDataSource.postLogin(jobseekerEmail, jobseekerPassword)
    }

    override fun postRegister(
        jobseekerName: String?,
        jobseekerEmail: String?,
        jobseekerPassword: String?
    ): Single<RegisterResponse> {
        return remoteDataSource.postRegister(jobseekerName, jobseekerEmail, jobseekerPassword)
    }

    override fun updateProfile(
        jobseekerId: RequestBody,
        jobseekerAbout: RequestBody,
        jobseekerName: RequestBody,
        jobseekerEmail: RequestBody,
        jobseekerPhone: RequestBody,
        jobseekerDateOfBirth: RequestBody,
        jobseekerAddress: RequestBody,
        jobseekerEducation: RequestBody,
        jobseekerProfession: RequestBody,
        jobseekerPortfolio: RequestBody,
        jobseekerSkill: RequestBody,
        jobseekerMedsos: RequestBody
    ): Single<UpdateProfileResponse> {
        return remoteDataSource.updateProfile(
            jobseekerId,
            jobseekerAbout,
            jobseekerName,
            jobseekerEmail,
            jobseekerPhone,
            jobseekerDateOfBirth,
            jobseekerAddress,
            jobseekerEducation,
            jobseekerProfession,
            jobseekerPortfolio,
            jobseekerSkill,
            jobseekerMedsos
        )
    }

    override fun updateImageProfile(
        jobseekerId: RequestBody,
        image: MultipartBody.Part
    ): Single<UpdateProfileResponse> {
        return remoteDataSource.updateImageProfile(jobseekerId, image)
    }

    override fun updateFileProfile(
        jobseekerId: RequestBody,
        file: MultipartBody.Part
    ): Single<UpdateProfileResponse> {
        return remoteDataSource.updateFileProfile(jobseekerId, file)
    }

    override fun postApply(
        jobId: RequestBody,
        jobseekerId: RequestBody
    ): Single<ApplyResponse> {
        return remoteDataSource.postApply(jobId, jobseekerId)
    }
}
