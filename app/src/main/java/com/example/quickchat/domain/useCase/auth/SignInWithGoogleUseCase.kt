package com.example.quickchat.domain.useCase.auth

import com.example.quickchat.data.auth.FirebaseAuthRepository

class SignInWithGoogleUseCase(
    private val accountService: FirebaseAuthRepository,
) {
    suspend operator fun invoke(idToken: String) {
        accountService.signInWithGoogle(idToken)
    }
}