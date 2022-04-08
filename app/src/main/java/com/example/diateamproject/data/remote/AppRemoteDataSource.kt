package com.example.diateamproject.data.remote

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
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AppRemoteDataSource {
    fun getAllJobs ():Single<AllJobsResponse>
    fun getRecentJobs ():Single<AllJobsResponse>

    fun getApplicationStatus (
        @Path("jobseekerId") id: Int?
    ): Single<ApplicationStatusResponse>
    fun getProfile (
        @Path("jobseekerId") id: Int?
    ): Single<ProfileResponse>

    fun getJobById(
        @Query("jobId") jobId: Int?,
        @Query("jobseekerId") jobseekerId: Int?,
    ): Single<JobByIdResponse>

    fun postLogin(
        @Query("jobseekerEmail") jobseekerEmail: String?,
        @Query("jobseekerPassword") jobseekerPassword: String?
    ): Single<LoginResponse>
    fun postRegister(
        @Query("jobseekerName") jobseekerName: String?,
        @Query("jobseekerEmail") jobseekerEmail: String?,
        @Query("jobseekerPassword") jobseekerPassword: String?
    ): Single<RegisterResponse>

    fun updateProfile(
        @Part("jobseekerId") jobseekerId: RequestBody,
        @Part("jobseekerAbout") jobseekerAbout: RequestBody,
        @Part("jobseekerName") jobseekerName: RequestBody,
        @Part("jobseekerEmail") jobseekerEmail: RequestBody,
        @Part("jobseekerPhone") jobseekerPhone: RequestBody,
        @Part("jobseekerDateOfBirth") jobseekerDateOfBirth: RequestBody,
        @Part("jobseekerAddress") jobseekerAddress: RequestBody,
        @Part("jobseekerEducation") jobseekerEducation: RequestBody,
        @Part("jobseekerProfession") jobseekerProfession: RequestBody,
        @Part("jobseekerPortfolio") jobseekerPortfolio: RequestBody,
        @Part("jobseekerSkill") jobseekerSkill: RequestBody,
        @Part("jobseekerMedsos") jobseekerMedsos: RequestBody
    ): Single<UpdateProfileResponse>

    fun updateImageProfile(
        @Part("jobseekerId") jobseekerId: RequestBody,
        @Part image: MultipartBody.Part
    ): Single<UpdateProfileResponse>

    fun updateFileProfile(
        @Part("jobseekerId") jobseekerId: RequestBody,
        @Part file: MultipartBody.Part
    ): Single<UpdateProfileResponse>

    fun postApply(
        @Part("jobId") jobId: RequestBody,
        @Part("jobseekerId") jobseekerId: RequestBody,
    ): Single<ApplyResponse>

}