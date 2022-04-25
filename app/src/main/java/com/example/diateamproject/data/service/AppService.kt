package com.example.diateamproject.data.service

import com.example.diateamproject.model.recentpostingjobs.RecentJobsResponse
import com.example.diateamproject.model.allpostingjobs.AllPostingJobsResponse
import com.example.diateamproject.model.applicationstatus.ApplicationStatusResponse
import com.example.diateamproject.model.apply.ApplyResponse
import com.example.diateamproject.model.applyjobstatus.ApplyJobStatusResponse
import com.example.diateamproject.model.emailresetpassword.EmailResetPasswordResponse
import com.example.diateamproject.model.jobbyid.JobByIdResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.profile.ProfileResponse
import com.example.diateamproject.model.updateprofile.UpdateProfileResponse
import com.example.diateamproject.model.register.RegisterResponse
import com.example.diateamproject.model.resetpassword.ResetPasswordResponse
import com.example.diateamproject.model.searchjob.SearchJobResponse
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

    @POST("api/v1/jobseeker/reset?")
    fun postEmailResetPassword(
        @Query("jobseekerEmail") jobseekerEmail: String?
    ): Single<EmailResetPasswordResponse>

    @POST("api/v1/jobseeker/change-password?")
    fun postResetPassword(
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Query("confirmPassword") confirmPassword: String?
    ): Single<ResetPasswordResponse>

    @GET("api/v1/jobseeker/job?")
    fun getJobById(
        @Query("jobId") jobId: Int?,
        @Query("jobseekerId") jobseekerId: Int?
    ): Single<JobByIdResponse>

    @GET("api/v1/jobseeker/pagination?")
    fun getAllPostingJob(
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Single<AllPostingJobsResponse>


    @PATCH("api/v1/jobseeker/user/update?")
    fun updateProfile(
        @Query("jobseekerId") jobseekerId: Int?,
        @Query("jobseekerAbout") jobseekerAbout: String?,
        @Query("jobseekerName") jobseekerName: String?,
        @Query("jobseekerEmail") jobseekerEmail: String?,
        @Query("jobseekerPhone") jobseekerPhone: String?,
        @Query("jobseekerDateOfBirth") jobseekerDateOfBirth: String?,
        @Query("jobseekerAddress") jobseekerAddress: String?,
        @Query("jobseekerEducation") jobseekerEducation: String?,
        @Query("jobseekerProfession") jobseekerProfession: String?,
        @Query("jobseekerPortfolio") jobseekerPortfolio: String?,
        @Query("jobseekerSkill") jobseekerSkill: String?,
        @Query("jobseekerMedsos") jobseekerMedsos: String?
    ): Single<UpdateProfileResponse>

    @GET("api/v1/jobseeker/search?")
    fun getSearchJob(
        @Query("keyword") keyword: String?,
    ): Single<SearchJobResponse>

    //request URL
    @GET("api/v1/jobseeker/jobs")
    fun getAllJobs(): Single<RecentJobsResponse>

    @GET("api/v1/jobseeker/recent")
    fun getRecentJobs(): Single<RecentJobsResponse>

    //request URL path {}
    @GET("api/v1/jobseeker/apply/status/{jobseekerId}")
    fun getApplicationStatus(
        @Path("jobseekerId") id: Int?
    ): Single<ApplicationStatusResponse>

    @GET("api/v1/jobseeker/user/{jobseekerId}")
    fun getProfile(
        @Path("jobseekerId") id: Int?
    ): Single<ProfileResponse>

    @GET("api/v1/jobseeker/apply/{jobseekerId}?")
    fun getApplyJobStatus(
        @Path("jobseekerId") jobseekerId: Int?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Single<ApplyJobStatusResponse>

    // request multipart - body [form-data]
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

    @Multipart
    @PUT("api/v1/jobseeker/job/apply")
    fun postApply(
        @Part("jobId") jobId: RequestBody,
        @Part("jobseekerId") jobseekerId: RequestBody
    ): Single<ApplyResponse>
}