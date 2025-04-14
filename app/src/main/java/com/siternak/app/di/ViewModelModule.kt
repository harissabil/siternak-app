package com.siternak.app.di

import com.siternak.app.ui.home.HomeViewModel
import com.siternak.app.ui.login.LoginViewModel
import com.siternak.app.ui.profile.ProfileViewModel
import com.siternak.app.ui.register.RegisterViewModel
import com.siternak.app.ui.user_form.UserFormViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::UserFormViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
}