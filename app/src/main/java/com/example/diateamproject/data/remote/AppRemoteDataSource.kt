package com.example.diateamproject.data.remote

import com.example.diateamproject.model.getalljob.JobResponse
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.requestlogin.RequestLogin
import io.reactivex.Single

interface AppRemoteDataSource {
    fun getAllJob ():Single<JobResponse>
    fun postLogin(requestLogin: RequestLogin):Single<LoginResponse>
}