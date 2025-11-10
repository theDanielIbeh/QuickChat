package com.example.quickchat.ui.presentation.screens.signUp

import androidx.credentials.Credential

sealed class SignUpEvent {
    data class SignUp(val popUpScreen: () -> Unit) : SignUpEvent()

    data class SignUpWithGoogle(val credential: Credential, val popUpScreen: () -> Unit) : SignUpEvent()


    data class EditEmail(val email: String) : SignUpEvent()

    data class EditPassword(val password: String) : SignUpEvent()

    data class EditConfirmPassword(val password: String) : SignUpEvent()

    data class EditPhone(val phone: String) : SignUpEvent()

    data object ResetState : SignUpEvent()
}