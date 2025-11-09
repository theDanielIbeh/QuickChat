package com.example.quickchat.domain.useCase.auth

import com.example.quickchat.data.auth.FirebaseAuthRepository

class SignInUseCase(
    private val accountService: FirebaseAuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ) {
        accountService.signInWithEmail(email, password)
    }
}