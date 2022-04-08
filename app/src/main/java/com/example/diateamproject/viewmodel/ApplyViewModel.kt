package com.example.diateamproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diateamproject.data.di.DaggerAppComponent
import com.example.diateamproject.data.repository.AppRepository
import com.example.diateamproject.model.apply.ApplyResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject

class ApplyViewModel : ViewModel(){
    val listApply = MutableLiveData<ApplyResponse>()
    private val compositeDisposable = CompositeDisposable()
    private val isError = MutableLiveData<Boolean>()

    @Inject
    lateinit var repository: AppRepository

    init {
        DaggerAppComponent.create().injectView(this)
    }

    fun postApply(jobId: RequestBody, jobseekerId: RequestBody) {
        compositeDisposable.add(
            repository.postApply(jobId, jobseekerId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApplyResponse>() {
                    override fun onSuccess(t: ApplyResponse) {
                        if (t.code == 200) {
                            listApply.value = t
                            Log.d("testApply", "notError = " + t.toString())
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
                                ApplyResponse::class.java
                            )
                            Log.d("testApplyError", "error = " + error)
                        }
                    }
                })
        )
    }

    fun responseApply(): MutableLiveData<ApplyResponse> {
        return listApply
    }

}