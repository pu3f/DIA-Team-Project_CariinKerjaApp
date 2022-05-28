package com.example.diateamproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diateamproject.data.di.DaggerAppComponent
import com.example.diateamproject.data.repository.AppRepository
import com.example.diateamproject.model.apply.ApplyResponse
import com.example.diateamproject.model.skills.SkillResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject

class SkillViewModel: ViewModel() {
    val listSkill = MutableLiveData<SkillResponse>()
    private val compositeDisposable = CompositeDisposable()
    private val isError = MutableLiveData<Boolean>()

    @Inject
    lateinit var repository: AppRepository

    init {
        DaggerAppComponent.create().injectView(this)
    }

    fun getSkill() {
        compositeDisposable.add(
            repository.getSkill()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SkillResponse>() {
                    override fun onSuccess(t: SkillResponse) {
                        if (t.code == 200) {
                            listSkill.value = t
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
                                SkillResponse::class.java
                            )
                            Log.d("testApplyError", "error = " + error)
                        }
                    }
                })
        )
    }

    fun responseSkill(): MutableLiveData<SkillResponse> {
        return listSkill
    }
}