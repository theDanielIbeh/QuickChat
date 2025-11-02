package com.example.quickchat.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Splash : Routes

    @Serializable
    data object Onboarding : Routes

    @Serializable
    data object Phone : Routes

    @Serializable
    data class Otp(val phone: String) : Routes

    @Serializable
    data object Home : Routes

    @Serializable
    data object Profile : Routes

    @Serializable
    data object ConversationList : Routes

    @Serializable
    data object Conversation : Routes

    @Serializable
    data object Settings : Routes
}