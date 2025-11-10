package com.example.quickchat.domain.useCase.username

import com.example.quickchat.data.auth.FirebaseAuthRepository

class ProfileUpdateUseCase(
    private val authRepository: FirebaseAuthRepository,
) {
    suspend operator fun invoke(name: String, imageUrl: String) {
        authRepository.updateUserProfile(name, imageUrl)
    }
}