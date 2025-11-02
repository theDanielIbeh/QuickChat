package com.example.quickchat.di

import com.example.quickchat.data.auth.FirebaseAuthRepository
import com.example.quickchat.data.auth.impl.FirebaseAuthRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseAuthRepository(impl: FirebaseAuthRepositoryImpl): FirebaseAuthRepository =
        impl

}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class FirebaseModule {
//
//    @Binds
//    abstract fun bindConnectivityObserver(networkConnectivityObserver: NetworkConnectivityObserver): ConnectivityObserver
//
//}