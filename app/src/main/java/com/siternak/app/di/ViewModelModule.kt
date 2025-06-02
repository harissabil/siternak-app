package com.siternak.app.di

import com.siternak.app.ui.home.HomeViewModel
import com.siternak.app.ui.login.LoginViewModel
import com.siternak.app.ui.pmk_detector.PmkDetectorViewModel
import com.siternak.app.ui.post.PostViewModel
import com.siternak.app.ui.post.add.AddPostViewModel
import com.siternak.app.ui.post.detail.PostDetailViewModel
import com.siternak.app.ui.profile.ProfileViewModel
import com.siternak.app.ui.profile.detail.ProfileDetailViewModel
import com.siternak.app.ui.register.RegisterViewModel
import com.siternak.app.ui.scan.ScanViewModel
import com.siternak.app.ui.user_form.UserFormViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::UserFormViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::PostViewModel)
    viewModelOf(::AddPostViewModel)
    viewModelOf(::PostDetailViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ProfileDetailViewModel)
    viewModelOf(::PmkDetectorViewModel)
    viewModelOf(::ScanViewModel)
}