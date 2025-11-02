package com.example.quickchat.di

import com.example.quickchat.data.network.connectivity.ConnectivityObserver
import com.example.quickchat.data.network.connectivity.NetworkConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//@Module
//@InstallIn(SingletonComponent::class)
//object ConnectivityModule {
//
//    @Provides
//    @Singleton
//    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver =
//        NetworkConnectivityObserver(context)
//
//}

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {

    @Binds
    abstract fun bindConnectivityObserver(networkConnectivityObserver: NetworkConnectivityObserver): ConnectivityObserver

}