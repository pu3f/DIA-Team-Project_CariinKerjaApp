package com.example.diateamproject.data.di

import com.example.diateamproject.viewmodel.FragmentJobViewModel
import com.example.diateamproject.viewmodel.LoginViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectView(loginViewModel: LoginViewModel)
    fun injectView(jobViewModel: FragmentJobViewModel)

}