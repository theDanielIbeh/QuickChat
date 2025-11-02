package com.example.quickchat.ui.presentation.screens.verification

import com.example.quickchat.ui.util.Constants.VERIFICATION_CODE_LENGTH
import com.google.firebase.auth.PhoneAuthProvider

data class VerificationState(
    val phone: String? = null,
    val code: List<Int?> = List(VERIFICATION_CODE_LENGTH, init = { null }),
    val focusedIndex: Int? = null,
    val isValid: Boolean? = null,
    val isTimerRunning: Boolean = true,
    val verificationId: String? = null,
    val resendToken: PhoneAuthProvider.ForceResendingToken? = null,
    val error: String? = null
)
