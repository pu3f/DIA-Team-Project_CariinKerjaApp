package com.example.diateamproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diateamproject.data.di.DaggerAppComponent
import com.example.diateamproject.data.repository.AppRepository
import com.example.diateamproject.model.login.LoginResponse
import com.example.diateamproject.model.register.RegisterResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class RegisterViewModel : ViewModel(){
    private val list = MutableLiveData<RegisterResponse>()
    private val compositeDisposable = CompositeDisposable()
    private val isError = MutableLiveData<Boolean>()

    @Inject
    lateinit var repository: AppRepository

    init {
        DaggerAppComponent.create().injectView(this)
    }

    fun postRegister(jobsekerName: String, jobseekerEmail: String, jobseekerPassword: String) {
        compositeDisposable.add(
            repository.postRegister(jobsekerName,jobseekerEmail,jobseekerPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<RegisterResponse>() {
                    override fun onSuccess(t: RegisterResponse) {
                        if (t.code == 200) {
                            list.value = t
                            Log.d("testLogin", "notError = " + t.toString())
                        } else {
                            isError.value = true
                        }
                    }

                    override fun onError(e: Throwable) {
                        isError.value = true
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                LoginResponse::class.java
                            )
                            Log.d("testLoginError", "error = "+ error)
                        }
                    }
                })
        )
    }

    fun listResponse(): MutableLiveData<RegisterResponse> {
        return list
    }

    fun getIsError(): MutableLiveData<Boolean> {
        return isError
    }
}