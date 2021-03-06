package com.example.diateamproject.data.di

import com.example.diateamproject.viewmodel.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectView(loginViewModel: LoginViewModel)
    fun injectView(registerViewModel: RegisterViewModel)
    fun injectView(recentJobViewModel: RecentJobViewModel)
    fun injectView(allJobViewModel: AllJobViewModel)
    fun injectView(applicationStatusViewModel: ApplicationStatusViewModel)
    fun injectView(profileViewModel: ProfileViewModel)
    fun injectView(applyViewModel: ApplyViewModel)
    fun injectView(jobDetailViewModel: JobDetailViewModel)
    fun injectView(emailResetPasswordViewModel: EmailResetPasswordViewModel)
    fun injectView(resetPasswordViewModel: ResetPasswordViewModel)
    fun injectView(searchJobViewModel: SearchJobViewModel)
    fun injectView(skillViewModel: SkillViewModel)
}