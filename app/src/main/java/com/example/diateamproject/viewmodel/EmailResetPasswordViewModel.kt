package com.example.diateamproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diateamproject.data.di.DaggerAppComponent
import com.example.diateamproject.data.repository.AppRepository
import com.example.diateamproject.model.emailresetpassword.EmailResetPasswordResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class EmailResetPasswordViewModel : ViewModel(){
    private val list = MutableLiveData<EmailResetPasswordResponse>()
    private val compositeDisposable = CompositeDisposable()
    private val isError = MutableLiveData<Boolean>()

    @Inject
    lateinit var repository: AppRepository

    init {
        DaggerAppComponent.create().injectView(this)
    }

    fun postEmailResetPassword(jobseekerEmail: String) {
        compositeDisposable.add(
            repository.postEmailResetPassword(jobseekerEmail)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<EmailResetPasswordResponse>() {
                    override fun onSuccess(t: EmailResetPasswordResponse) {
                        if (t.code == 200) {
                            list.value = t
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
                                EmailResetPasswordResponse::class.java
                            )
                            Log.d("testEmailError", "error = "+ error)
                        }
                    }
                })
        )
    }

    fun listResponse(): MutableLiveData<EmailResetPasswordResponse> {
        return list
    }

    fun getIsError(): MutableLiveData<Boolean> {
        return isError
    }

}