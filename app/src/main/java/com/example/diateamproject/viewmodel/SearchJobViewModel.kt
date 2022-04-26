package com.example.diateamproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diateamproject.data.di.DaggerAppComponent
import com.example.diateamproject.data.repository.AppRepository
import com.example.diateamproject.model.searchjob.SearchJobResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class SearchJobViewModel : ViewModel() {
    private val listJobs = MutableLiveData<SearchJobResponse>()
    private val compositeDisposable = CompositeDisposable()
    private val isError = MutableLiveData<Boolean>()

    @Inject
    lateinit var repository: AppRepository

    init {
        DaggerAppComponent.create().injectView(this)
    }

    fun getSearchJob(keyword: String) {
        compositeDisposable.add(
            repository.getSearchJob(keyword)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SearchJobResponse>() {
                    override fun onSuccess(t: SearchJobResponse) {
                        if (t.code == 200) {
                            listJobs.value = t
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
                                SearchJobResponse::class.java
                            )
                            Log.d("testEmailError", "error = "+ error)
                        }
                    }
                })
        )
    }

    fun listJobsResponse(): MutableLiveData<SearchJobResponse> {
        return listJobs
    }

    fun getIsError(): MutableLiveData<Boolean> {
        return isError
    }
}
