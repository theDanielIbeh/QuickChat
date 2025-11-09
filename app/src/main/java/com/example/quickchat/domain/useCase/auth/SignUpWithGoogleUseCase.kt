package com.example.quickchat.domain.useCase.auth

import com.example.quickchat.data.auth.FirebaseAuthRepository

class SignUpWithGoogleUseCase(
    private val accountService: FirebaseAuthRepository,
) {
    suspend operator fun invoke(idToken: String) {
        accountService.linkAccountWithGoogle(idToken)
    }
}