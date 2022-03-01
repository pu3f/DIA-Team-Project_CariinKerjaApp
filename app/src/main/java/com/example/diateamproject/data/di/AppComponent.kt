package com.example.diateamproject.data.di

import com.example.diateamproject.viewmodel.AllJobViewModel
import com.example.diateamproject.viewmodel.RecentJobViewModel
import com.example.diateamproject.viewmodel.LoginViewModel
import com.example.diateamproject.viewmodel.RegisterViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectView(loginViewModel: LoginViewModel)
    fun injectView(registerViewModel: RegisterViewModel)
    fun injectView(recentJobViewModel: RecentJobViewModel)
    fun injectView(allJobViewModel: AllJobViewModel)
}