package com.team.eatcleanapp.di

import com.team.eatcleanapp.data.remote.firebase.FirebaseAuthDataSource
import com.team.eatcleanapp.data.remote.firebase.RealtimeDatabaseService

object FirebaseModule {

    val firebaseAuthDataSource: FirebaseAuthDataSource by lazy {
        FirebaseAuthDataSource()
    }

    val realtimeDatabaseService: RealtimeDatabaseService by lazy {
        RealtimeDatabaseService()
    }
}