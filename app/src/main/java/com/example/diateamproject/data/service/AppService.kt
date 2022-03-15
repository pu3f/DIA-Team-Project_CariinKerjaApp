package com.example.diateamproject.data.service

import com.example.diateamproject.model.alljobs.AllJobsResponse
import com.example.diateamproject.model.applicationstatus.ApplicationStatusResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.register.RegisterResponse
import io.reactivex.Single
import retrofit2.http.*

interface AppService {
    //    request params
    @POST("api/v1/jobseeker/login?")
    fun postLogin(
        @Query("jobseekerEmail") jobseekerEmail: String?,
        @Query("jobseekerPassword") jobseekerPassword: String?
    ): Single<LoginResponse>

    @POST("api/v1/jobseeker/register?")
    fun postRegister(
        @Query("jobseekerName") jobseekerName: String?,
        @Query("jobseekerEmail") jobseekerEmail: String?,
        @Query("jobseekerPassword") jobseekerPassword: String?
    ): Single<RegisterResponse>

    @GET("api/v1/jobseeker/jobs")
    fun getAllJobs(): Single<AllJobsResponse>

    @GET("api/v1/jobseeker/recent")
    fun getRecentJobs(): Single<AllJobsResponse>

    @GET("api/v1/jobseeker/apply/9")
    fun getApplicationStatus(): Single<ApplicationStatusResponse>

}