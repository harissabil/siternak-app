package com.siternak.app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siternak.app.data.firestore.FirestoreRepositoryImpl
import com.siternak.app.domain.repository.FirestoreRepository
import org.koin.dsl.module

val firestoreModule = module {
    single<FirestoreRepository> {
        FirestoreRepositoryImpl(
            auth = FirebaseAuth.getInstance(),
            userDataRef = Firebase.firestore.collection("user_data"),
            postRef = Firebase.firestore.collection("post"),
        )
    }
}