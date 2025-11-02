package com.example.quickchat.ui.presentation.screens.phoneNumber

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickchat.data.auth.FirebaseAuthRepository
import com.example.quickchat.ui.presentation.screens.verification.OtpAction
import com.example.quickchat.ui.presentation.screens.verification.VerificationState
import com.example.quickchat.ui.util.Constants.VERIFICATION_TIMEOUT_SECONDS
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@HiltViewModel
class PhoneNumberViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {



}