package com.example.diateamproject.data.remote

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
import com.example.diateamproject.model.skills.SkillResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AppRemoteDataSource {
    fun getAllJobs ():Single<RecentJobsResponse>
    fun getRecentJobs ():Single<RecentJobsResponse>
    fun getSkill(): Single<SkillResponse>

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

    fun getApplyJobStatus(
        @Path("jobseekerId") jobseekerId: Int?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Single<ApplyJobStatusResponse>

    fun getAllPostingJob(
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): Single<AllPostingJobsResponse>

    fun postLogin(
        @Query("jobseekerEmail") jobseekerEmail: String?,
        @Query("jobseekerPassword") jobseekerPassword: String?
    ): Single<LoginResponse>

    fun postRegister(
        @Query("jobseekerName") jobseekerName: String?,
        @Query("jobseekerEmail") jobseekerEmail: String?,
        @Query("jobseekerPassword") jobseekerPassword: String?
    ): Single<RegisterResponse>

    fun postEmailResetPassword(
        @Query("jobseekerEmail") jobseekerEmail: String?
    ): Single<EmailResetPasswordResponse>

    fun postResetPassword(
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Query("confirmPassword") confirmPassword: String?
    ): Single<ResetPasswordResponse>

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

    fun getSearchJob(
        @Query("keyword") keyword: String?,
    ): Single<SearchJobResponse>

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