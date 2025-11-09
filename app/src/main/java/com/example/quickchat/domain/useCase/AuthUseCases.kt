package com.example.quickchat.domain.useCase

import com.example.quickchat.domain.useCase.auth.SignInUseCase
import com.example.quickchat.domain.useCase.auth.SignInWithGoogleUseCase
import com.example.quickchat.domain.useCase.auth.SignUpUseCase
import com.example.quickchat.domain.useCase.auth.SignUpWithGoogleUseCase

data class AuthUseCases(
    val signIn: SignInUseCase,
    val signUp: SignUpUseCase,
    val signUpWithGoogle: SignUpWithGoogleUseCase,
    val signInWithGoogle: SignInWithGoogleUseCase,
)