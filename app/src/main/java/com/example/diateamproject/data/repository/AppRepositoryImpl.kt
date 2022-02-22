package com.example.diateamproject.data.repository

import com.example.diateamproject.model.getalljob.JobResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.requestlogin.RequestLogin
import com.example.diateamproject.data.remote.AppRemoteDataSource
import io.reactivex.Single
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(private val remoteDataSource: AppRemoteDataSource):
    AppRepository {
    override fun getAllJob(): Single<JobResponse> {
        return remoteDataSource.getAllJob()
    }

    override fun postLogin(requestLogin: RequestLogin): Single<LoginResponse> {
        return remoteDataSource.postLogin(requestLogin)
    }
}