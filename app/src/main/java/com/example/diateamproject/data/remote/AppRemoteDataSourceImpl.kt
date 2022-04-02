package com.example.diateamproject.data.remote

import com.example.diateamproject.data.service.AppService
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

class AppRemoteDataSourceImpl @Inject constructor(private val service: AppService) :
    AppRemoteDataSource {

    override fun getAllJobs(): Single<AllJobsResponse> {
        return service.getAllJobs()
    }

    override fun getRecentJobs(): Single<AllJobsResponse> {
        return service.getRecentJobs()
    }

    override fun getApplicationStatus(id: Int?): Single<ApplicationStatusResponse> {
        return service.getApplicationStatus(id)
    }

    override fun getProfile(id: Int?): Single<ProfileResponse> {
        return service.getProfile(id)
    }

    override fun getJobById(id: Int?): Single<JobByIdResponse> {
        return service.getJobById(id)
    }

    override fun postLogin(
        jobseekerEmail: String?,
        jobseekerPassword: String?
    ): Single<LoginResponse> {
        return service.postLogin(jobseekerEmail, jobseekerPassword)
    }

    override fun postRegister(
        jobseekerName: String?,
        jobseekerEmail: String?,
        jobseekerPassword: String?
    ): Single<RegisterResponse> {
        return service.postRegister(jobseekerName, jobseekerEmail, jobseekerPassword)

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
        return service.updateProfile(
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
        return service.updateImageProfile(jobseekerId, image)
    }

    override fun updateFileProfile(
        jobseekerId: RequestBody,
        file: MultipartBody.Part
    ): Single<UpdateProfileResponse> {
        return service.updateFileProfile(jobseekerId, file)
    }

    override fun postApply(
        jobId: RequestBody,
        jobseekerId: RequestBody
    ): Single<ApplyResponse> {
        return service.postApply(jobId, jobseekerId)
    }

}
