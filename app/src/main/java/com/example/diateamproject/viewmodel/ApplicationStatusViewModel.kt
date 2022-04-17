package com.example.diateamproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diateamproject.data.di.DaggerAppComponent
import com.example.diateamproject.data.repository.AppRepository
import com.example.diateamproject.model.applyjobstatus.ApplyJobStatusResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class ApplicationStatusViewModel : ViewModel() {
    private val allList = MutableLiveData<ApplyJobStatusResponse>()
    private val compositeDisposable = CompositeDisposable()
    private val isError = MutableLiveData<Boolean>()

    @Inject
    lateinit var repository: AppRepository

    init {
        DaggerAppComponent.create().injectView(this)
    }

    fun getApplyJobStatus(jobseekerId: Int, page: Int, size: Int) {
        compositeDisposable.add(
            repository.getApplyJobStatus(jobseekerId, page, size)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApplyJobStatusResponse>() {
                    override fun onSuccess(t: ApplyJobStatusResponse) {
                        allList.value = t
                        Log.d("testJob", "notError = " + t.toString())
                    }

                    override fun onError(e: Throwable) {
                        Log.d("testJobError", "Error = " + e.toString())
                        isError.value = true
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                ApplyJobStatusResponse::class.java
                            )
                            Log.d("testJobError", "Error = " + error)
                        }
                    }
                })
        )
    }

    fun allListResponse(): MutableLiveData<ApplyJobStatusResponse> {
        return allList
    }

    fun getIsError(): MutableLiveData<Boolean> {
        return isError
    }
}