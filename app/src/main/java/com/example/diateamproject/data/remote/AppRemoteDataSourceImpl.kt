package com.example.diateamproject.data.remote

import com.example.diateamproject.data.service.AppService
import com.example.diateamproject.model.alljobs.AllJobsResponse
import com.example.diateamproject.model.applicationstatus.ApplicationStatusResponse
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

    override fun getApplicationStatus(Id: Int?): Single<ApplicationStatusResponse> {
        return service.getApplicationStatus(Id)
    }

    override fun getProfile(Id: Int?): Single<ProfileResponse> {
        return service.getProfile(Id)
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
        jobseekerEducation: RequestBody
    ): Single<UpdateProfileResponse> {
        return service.updateProfile(
            jobseekerId,
            jobseekerAbout,
            jobseekerName,
            jobseekerEmail,
            jobseekerPhone,
            jobseekerDateOfBirth,
            jobseekerAddress,
            jobseekerEducation
        )
    }

    override fun updateImageProfile(jobseekerId: RequestBody, image: MultipartBody.Part): Single<UpdateProfileResponse> {
        return service.updateImageProfile(jobseekerId, image)
    }

    override fun updateFileProfile(jobseekerId: RequestBody, file: MultipartBody.Part): Single<UpdateProfileResponse> {
        return service.updateFileProfile(jobseekerId, file)
    }

}
