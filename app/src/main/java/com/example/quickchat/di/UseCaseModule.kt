package com.example.quickchat.di

import com.example.quickchat.data.auth.FirebaseAuthRepository
import com.example.quickchat.domain.useCase.AuthUseCases
import com.example.quickchat.domain.useCase.auth.SignInUseCase
import com.example.quickchat.domain.useCase.auth.SignInWithGoogleUseCase
import com.example.quickchat.domain.useCase.auth.SignUpUseCase
import com.example.quickchat.domain.useCase.auth.SignUpWithGoogleUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideAuthUseCases(
        repository: FirebaseAuthRepository
    ): AuthUseCases {
        return AuthUseCases(
            signIn = SignInUseCase(accountService = repository),
            signUp = SignUpUseCase(accountService = repository),
            signInWithGoogle = SignInWithGoogleUseCase(accountService = repository),
            signUpWithGoogle = SignUpWithGoogleUseCase(accountService = repository)
        )
    }

}