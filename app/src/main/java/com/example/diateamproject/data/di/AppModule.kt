package com.example.diateamproject.data.di

import com.example.diateamproject.data.remote.AppRemoteDataSource
import com.example.diateamproject.data.remote.AppRemoteDataSourceImpl
import com.example.diateamproject.data.repository.AppRepository
import com.example.diateamproject.data.repository.AppRepositoryImpl
import com.example.diateamproject.data.service.AppRetrofit
import com.example.diateamproject.data.service.AppService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideRepository(appRemoteDataSource: AppRemoteDataSource): AppRepository {
        return AppRepositoryImpl(appRemoteDataSource)
    }
    @Provides
    @Singleton
    fun provideRemoteDataSource(appService: AppService): AppRemoteDataSource {
        return AppRemoteDataSourceImpl(appService)
    }
    @Provides
    @Singleton
    fun provideAppService(): AppService {
        return AppRetrofit.appService
    }
}