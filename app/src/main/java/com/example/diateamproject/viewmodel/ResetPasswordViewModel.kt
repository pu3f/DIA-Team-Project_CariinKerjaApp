package com.example.diateamproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diateamproject.data.di.DaggerAppComponent
import com.example.diateamproject.data.repository.AppRepository
import com.example.diateamproject.model.resetpassword.ResetPasswordResponse
import com.example.diateamproject.model.verifyresetpassword.VerifyResetPasswordResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class ResetPasswordViewModel : ViewModel() {
    private val responseVerify = MutableLiveData<VerifyResetPasswordResponse>()
    private val listResetPassword = MutableLiveData<ResetPasswordResponse>()
    private val compositeDisposable = CompositeDisposable()
    private val isError = MutableLiveData<Boolean>()

    @Inject
    lateinit var repository: AppRepository

    init {
        DaggerAppComponent.create().injectView(this)
    }

    fun getVerifyResetPassword(token: String) {
        compositeDisposable.add(
            repository.getVerifyResetPassword(token)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<VerifyResetPasswordResponse>() {
                    override fun onSuccess(t: VerifyResetPasswordResponse) {
                        if (t.code == 200) {
                            responseVerify.value = t
                            Log.d("testEmail", "notError = " + t.toString())
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
                                VerifyResetPasswordResponse::class.java
                            )
                            Log.d("testEmailError", "error = "+ error)
                        }
                    }
                })
        )
    }

    fun postResetPassword(email: String, password: String, confirmPassword: String ) {
        compositeDisposable.add(
            repository.postResetPassword(email, password, confirmPassword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResetPasswordResponse>() {
                    override fun onSuccess(t: ResetPasswordResponse) {
                        if (t.code == 200) {
                            listResetPassword.value = t
                            Log.d("testEmail", "notError = " + t.toString())
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
                                ResetPasswordResponse::class.java
                            )
                            Log.d("testEmailError", "error = "+ error)
                        }
                    }
                })
        )
    }

    fun responseVerify(): MutableLiveData<VerifyResetPasswordResponse> {
        return responseVerify
    }

    fun listResetPasswordResponse(): MutableLiveData<ResetPasswordResponse> {
        return listResetPassword
    }

    fun getIsError(): MutableLiveData<Boolean> {
        return isError
    }
}