package com.example.diateamproject.data.repository

import com.example.diateamproject.data.remote.AppRemoteDataSource
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
import com.example.diateamproject.model.verifyresetpassword.VerifyResetPasswordResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(private val remoteDataSource: AppRemoteDataSource) :
    AppRepository {

    override fun getAllJobs(): Single<RecentJobsResponse> {
        return remoteDataSource.getAllJobs()
    }

    override fun getRecentJobs(): Single<RecentJobsResponse> {
        return remoteDataSource.getRecentJobs()
    }

    override fun getApplicationStatus(id: Int?): Single<ApplicationStatusResponse> {
        return remoteDataSource.getApplicationStatus(id)
    }

    override fun getProfile(id: Int?): Single<ProfileResponse> {
        return remoteDataSource.getProfile(id)
    }

    override fun getJobById(jobId: Int?, jobseekerId: Int?): Single<JobByIdResponse> {
        return remoteDataSource.getJobById(jobId, jobseekerId)
    }

    override fun getApplyJobStatus(
        jobseekerId: Int?,
        page: Int?,
        size: Int?
    ): Single<ApplyJobStatusResponse> {
        return remoteDataSource.getApplyJobStatus(jobseekerId, page, size)
    }

    override fun getVerifyResetPassword(token: String?): Single<VerifyResetPasswordResponse> {
        return remoteDataSource.getVerifyResetPassword(token)
    }

    override fun getAllPostingJob(page: Int?, size: Int?): Single<AllPostingJobsResponse> {
        return remoteDataSource.getAllPostingJob(page, size)
    }

    override fun postLogin(
        jobseekerEmail: String?,
        jobseekerPassword: String?
    ): Single<LoginResponse> {
        return remoteDataSource.postLogin(jobseekerEmail, jobseekerPassword)
    }

    override fun postRegister(
        jobseekerName: String?,
        jobseekerEmail: String?,
        jobseekerPassword: String?
    ): Single<RegisterResponse> {
        return remoteDataSource.postRegister(jobseekerName, jobseekerEmail, jobseekerPassword)
    }

    override fun postEmailResetPassword(jobseekerEmail: String?): Single<EmailResetPasswordResponse> {
        return remoteDataSource.postEmailResetPassword(jobseekerEmail)
    }

    override fun postResetPassword(
        email: String?,
        password: String?,
        confirmPassword: String?
    ): Single<ResetPasswordResponse> {
        return remoteDataSource.postResetPassword(email, password, confirmPassword)
    }

    override fun updateProfile(
        jobseekerId: Int?,
        jobseekerAbout: String?,
        jobseekerName: String?,
        jobseekerEmail: String?,
        jobseekerPhone: String?,
        jobseekerDateOfBirth: String?,
        jobseekerAddress: String?,
        jobseekerEducation: String?,
        jobseekerProfession: String?,
        jobseekerPortfolio: String?,
        jobseekerSkill: String?,
        jobseekerMedsos: String?
    ): Single<UpdateProfileResponse> {
        return remoteDataSource.updateProfile(
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

    override fun getSearchJob(keyword: String?): Single<JobByIdResponse> {
        return remoteDataSource.getSearchJob(keyword)
    }

    override fun updateImageProfile(
        jobseekerId: RequestBody,
        image: MultipartBody.Part
    ): Single<UpdateProfileResponse> {
        return remoteDataSource.updateImageProfile(jobseekerId, image)
    }

    override fun updateFileProfile(
        jobseekerId: RequestBody,
        file: MultipartBody.Part
    ): Single<UpdateProfileResponse> {
        return remoteDataSource.updateFileProfile(jobseekerId, file)
    }

    override fun postApply(
        jobId: RequestBody,
        jobseekerId: RequestBody
    ): Single<ApplyResponse> {
        return remoteDataSource.postApply(jobId, jobseekerId)
    }
}
