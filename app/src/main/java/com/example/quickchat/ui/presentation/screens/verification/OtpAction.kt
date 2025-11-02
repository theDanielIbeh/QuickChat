package com.example.quickchat.ui.presentation.screens.verification

import android.app.Activity

sealed interface OtpAction {
    data class OnEnterNumber(val number: Int?, val index: Int) : OtpAction
    data class OnChangeFieldFocus(val index: Int?) : OtpAction
    data class StartPhoneNumberVerification(val phoneNumber: String, val activity: Activity) : OtpAction

    data class OnResendVerificationCode(val phoneNumber: String, val activity: Activity) : OtpAction

    data class OnVerifyPhoneNumber(val code: String) : OtpAction
    object OnKeyboardBackspace : OtpAction
}