package com.example.diateamproject.data.service

import com.example.diateamproject.model.alljobs.AllJobsResponse
import com.example.diateamproject.model.applicationstatus.ApplicationStatusResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.profile.ProfileResponse
import com.example.diateamproject.model.updateprofile.UpdateProfileResponse
import com.example.diateamproject.model.register.RegisterResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AppService {
    //request query params
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

    //request URL
    @GET("api/v1/jobseeker/jobs")
    fun getAllJobs(): Single<AllJobsResponse>

    @GET("api/v1/jobseeker/recent")
    fun getRecentJobs(): Single<AllJobsResponse>

    //request URL path {}
    @GET("api/v1/jobseeker/apply/{jobseekerId}")
    fun getApplicationStatus(
        @Path("jobseekerId") id: Int?
    ): Single<ApplicationStatusResponse>

    @GET("api/v1/jobseeker/user/{jobseekerId}")
    fun getProfile(
        @Path("jobseekerId") id: Int?
    ): Single<ProfileResponse>

    // request multipart
    @Multipart
    @PATCH("api/v1/jobseeker/user/update")
    fun updateProfile(
        @Part("jobseekerId") jobseekerId: RequestBody,
        @Part("jobseekerAbout") jobseekerAbout: RequestBody,
        @Part("jobseekerName") jobseekerName: RequestBody,
        @Part("jobseekerEmail") jobseekerEmail: RequestBody,
        @Part("jobseekerPhone") jobseekerPhone: RequestBody,
        @Part("jobseekerDateOfBirth") jobseekerDateOfBirth: RequestBody,
        @Part("jobseekerAddress") jobseekerAddress: RequestBody,
        @Part("jobseekerEducation") jobseekerEducation: RequestBody,
    ): Single<UpdateProfileResponse>

    @Multipart
    @POST("api/v1/jobseeker/user/update/image")
    fun updateImageProfile(
        @Part("jobseekerId") jobseekerId: RequestBody,
        @Part image: MultipartBody.Part
    ): Single<UpdateProfileResponse>

    @Multipart
    @POST("api/v1/jobseeker/user/update/resume")
    fun updateFileProfile(
        @Part("jobseekerId") jobseekerId: RequestBody,
        @Part file: MultipartBody.Part
    ): Single<UpdateProfileResponse>
}