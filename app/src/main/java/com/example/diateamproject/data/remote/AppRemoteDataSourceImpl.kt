package com.example.diateamproject.data.remote

import com.example.diateamproject.model.getalljob.JobResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.requestlogin.RequestLogin
import com.example.diateamproject.data.service.AppService
import io.reactivex.Single
import javax.inject.Inject

class AppRemoteDataSourceImpl @Inject constructor(private val service: AppService):
    AppRemoteDataSource {
    override fun getAllJob(): Single<JobResponse> {
        return service.getAllJob()
    }

    override fun postLogin(requestLogin: RequestLogin): Single<LoginResponse> {
        return service.postLogin(requestLogin)

    }

}