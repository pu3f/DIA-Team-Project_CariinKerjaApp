package com.example.diateamproject.data.repository

import com.example.diateamproject.data.remote.AppRemoteDataSource
import com.example.diateamproject.model.alljobs.AllJobsResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.register.RegisterResponse
import io.reactivex.Single
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(private val remoteDataSource: AppRemoteDataSource):
    AppRepository {

    override fun getAllJobs(): Single<AllJobsResponse> {
        return remoteDataSource.getAllJobs()
    }

    override fun getRecentJobs(): Single<AllJobsResponse> {
        return remoteDataSource.getRecentJobs()
    }

    override fun postLogin(
        jobseekerEmail: String?,
        jobseekerPassword: String?
    ): Single<LoginResponse> {
        return remoteDataSource.postLogin(jobseekerEmail,jobseekerPassword)
    }

    override fun postRegister(
        jobseekerName: String?,
        jobseekerEmail: String?,
        jobseekerPassword: String?
    ): Single<RegisterResponse> {
        return remoteDataSource.postRegister(jobseekerName,jobseekerEmail,jobseekerPassword)
    }
}
