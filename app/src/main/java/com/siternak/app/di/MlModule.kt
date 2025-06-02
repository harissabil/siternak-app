package com.siternak.app.di

import com.siternak.app.data.ml.PMKLidahClassifier
import org.koin.dsl.module

val mlModule = module {
    single { PMKLidahClassifier(context = get()) }
}