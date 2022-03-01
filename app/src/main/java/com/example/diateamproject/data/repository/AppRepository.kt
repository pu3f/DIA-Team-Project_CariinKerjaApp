package com.example.diateamproject.data.repository

import com.example.diateamproject.model.alljobs.AllJobsResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.register.RegisterResponse
import io.reactivex.Single
import retrofit2.http.Query

interface AppRepository {
    fun getAllJobs (): Single<AllJobsResponse>
    fun getRecentJobs (): Single<AllJobsResponse>
    fun postLogin(
        @Query("jobseekerEmail") jobseekerEmail: String?,
        @Query("jobseekerPassword") jobseekerPassword: String?
    ): Single<LoginResponse>
    fun postRegister(
        @Query("jobseekerName") jobseekerName: String?,
        @Query("jobseekerEmail") jobseekerEmail: String?,
        @Query("jobseekerPassword") jobseekerPassword: String?
    ): Single<RegisterResponse>
}