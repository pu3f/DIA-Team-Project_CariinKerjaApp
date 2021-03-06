package com.example.diateamproject.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diateamproject.data.di.DaggerAppComponent
import com.example.diateamproject.data.repository.AppRepository
import com.example.diateamproject.model.profile.ProfileResponse
import com.example.diateamproject.model.updateprofile.UpdateProfileResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject

class ProfileViewModel : ViewModel() {
    private val responseProfile = MutableLiveData<ProfileResponse>()
    private val listProfile = MutableLiveData<UpdateProfileResponse>()
    private val listImage = MutableLiveData<UpdateProfileResponse>()
    private val listFile = MutableLiveData<UpdateProfileResponse>()
    private val compositeDisposable = CompositeDisposable()
    private val isError = MutableLiveData<Boolean>()
    private val isErrorUpdate = MutableLiveData<Boolean>()
    private val isErrorFile = MutableLiveData<Boolean>()

    @Inject
    lateinit var repository: AppRepository

    init {
        DaggerAppComponent.create().injectView(this)
    }

    fun getProfile(id: Int) {
        compositeDisposable.add(
            repository.getProfile(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ProfileResponse>() {
                    override fun onSuccess(t: ProfileResponse) {
                        if (t.code == 200) {
                            responseProfile.value = t
                            Log.d("testProfile", "notError = " + t.toString())
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
                                ProfileResponse::class.java
                            )
                            Log.d("testGetProfileError", "error = " + error)
                        }
                    }
                })
        )
    }

    fun updateProfile(
        jobseekerId: Int?,
        jobseekerAbout: String?,
        jobseekerName: String?,
        jobseekerEmail: String?,
        jobseekerPhone: String?,
        jobseekerDateOfBirth: String?,
        jobseekerAddress: String?,
        jobseekerEducation: String?,
        jobseekerProfession: String?,
        jobseekerPortfolio: String?,
        jobseekerSkill: String?,
        jobseekerMedsos: String?,
        jobsekerCompany: String?,
        workStartYear: Int?,
        workEndYear: Int?
    ) {
        compositeDisposable.add(
            repository.updateProfile(
                jobseekerId,
                jobseekerAbout,
                jobseekerName,
                jobseekerEmail,
                jobseekerPhone,
                jobseekerDateOfBirth,
                jobseekerAddress,
                jobseekerEducation,
                jobseekerProfession,
                jobseekerPortfolio,
                jobseekerSkill,
                jobseekerMedsos,
                jobsekerCompany,
                workStartYear,
                workEndYear
            )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateProfileResponse>() {
                    override fun onSuccess(t: UpdateProfileResponse) {
                        if (t.code == 200) {
                            listProfile.value = t
                            Log.d("testUpdateProfile", "notError = " + t.toString())
                        } else {
                            isErrorUpdate.value = true
                        }
                    }

                    override fun onError(e: Throwable) {
                        isErrorUpdate.value = true
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UpdateProfileResponse::class.java
                            )
                            Log.d("testUpdateProfileError", "error = " + error)
                        }
                    }
                })
        )
    }

    fun updateImageProfile(jobseekerId: RequestBody, image: MultipartBody.Part) {
        compositeDisposable.add(
            repository.updateImageProfile(jobseekerId, image)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateProfileResponse>() {
                    override fun onSuccess(t: UpdateProfileResponse) {
                        if (t.code == 200) {
                            listImage.value = t
                            Log.d("testImageProfile", "notError = " + t.toString())
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
                                UpdateProfileResponse::class.java
                            )
                            Log.d("testImageProfileError", "error = " + error)
                        }
                    }
                })
        )
    }

    fun updateFileProfile(jobseekerId: RequestBody, file: MultipartBody.Part) {
        compositeDisposable.add(
            repository.updateFileProfile(jobseekerId, file)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UpdateProfileResponse>() {
                    override fun onSuccess(t: UpdateProfileResponse) {
                        if (t.code == 200) {
                            listFile.value = t
                            Log.d("testFileProfile", "notError = " + t.toString())
                        } else {
                            isErrorFile.value = true
                        }
                    }

                    override fun onError(e: Throwable) {
                        isErrorFile.value = true
                        if (e is HttpException) {
                            val errorBody = (e as HttpException).response()?.errorBody()
                            val gson = Gson()
                            val error = gson.fromJson(
                                errorBody?.string(),
                                UpdateProfileResponse::class.java
                            )
                            Log.d("testFileProfileError", "error = " + error.toString())
                        }
                    }
                })
        )
    }


    fun responseProfile(): MutableLiveData<ProfileResponse> {
        return responseProfile
    }

    fun listResponseProfile(): MutableLiveData<UpdateProfileResponse> {
        return listProfile
    }

    fun listResponseImage(): MutableLiveData<UpdateProfileResponse> {
        return listImage
    }

    fun listResponseFile(): MutableLiveData<UpdateProfileResponse> {
        return listFile
    }

    fun getIsError(): MutableLiveData<Boolean> {
        return isError
    }

    fun getIsErrorUpdate(): MutableLiveData<Boolean> {
        return isErrorUpdate
    }

    fun getIsErrorFile(): MutableLiveData<Boolean> {
        return isErrorFile
    }
}