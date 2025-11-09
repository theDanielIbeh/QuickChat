package com.example.quickchat.ui.presentation.screens.userName

import androidx.credentials.Credential

sealed class UsernameEvent {
    data class OnContinue(val popUpScreen: (String) -> Unit) : UsernameEvent()

    data class EditUsername(val username: String) : UsernameEvent()

    data class EditPhoto(val password: String) : UsernameEvent()

    data object ResetState : UsernameEvent()
}