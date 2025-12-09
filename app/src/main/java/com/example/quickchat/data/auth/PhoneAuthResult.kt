package com.example.quickchat.data.auth

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed class PhoneAuthResult {
    data class CodeSent(val verificationId: String, val token: PhoneAuthProvider.ForceResendingToken) : PhoneAuthResult()
    data class VerificationCompleted(val credential: PhoneAuthCredential) : PhoneAuthResult()
    data class VerificationFailed(val exception: FirebaseException) : PhoneAuthResult()
}
