package com.example.quickchat.ui.presentation.screens.signIn

import androidx.credentials.Credential

sealed class SignInEvent {
    data class SignIn(val popUpScreen: () -> Unit) : SignInEvent()

    data class SignInWithGoogle(val credential: Credential, val popUpScreen: () -> Unit) : SignInEvent()

    data class EditEmail(val email: String) : SignInEvent()

    data class EditPassword(val password: String) : SignInEvent()

    data object ResetState : SignInEvent()
}