package com.example.quickchat.data.network.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    val isConnected: Flow<Boolean>
}