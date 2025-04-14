package com.siternak.app.di

import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.siternak.app.data.auth.AuthRepositoryImpl
import com.siternak.app.domain.repository.AuthRepository
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(
            CredentialManager.create(get()),
            FirebaseAuth.getInstance()
        )
    }
}