package com.example.diateamproject.data.remote

import com.example.diateamproject.data.service.AppService
import com.example.diateamproject.model.alljobs.AllJobsResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.register.RegisterResponse
import io.reactivex.Single
import javax.inject.Inject

class AppRemoteDataSourceImpl @Inject constructor(private val service: AppService) :
    AppRemoteDataSource {

    override fun getAllJobs(): Single<AllJobsResponse> {
        return service.getAllJobs()
    }

    override fun getRecentJobs(): Single<AllJobsResponse> {
        return service.getRecentJobs()
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

}
