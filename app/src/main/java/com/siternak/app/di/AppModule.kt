package com.siternak.app.di

import org.koin.dsl.module

val appModule = module {
    includes(
        authModule,
        firestoreModule,
        viewModelModule,
        mlModule
    )
}