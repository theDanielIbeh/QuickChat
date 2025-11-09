package com.example.quickchat.ui.presentation.screens.signIn

import com.example.quickchat.R
import com.example.quickchat.ui.util.State.ErrorState


/**
 * Error state in login holding respective
 * text field validation errors
 */
data class SignInErrorState(
    val emailErrorState: ErrorState = ErrorState(),
    val passwordErrorState: ErrorState = ErrorState(),
)

/**
 * Login State holding ui input values
 */
data class SignInState(
    val email: String = "",
    val password: String = "",
    val errorState: SignInErrorState = SignInErrorState(),
)

val emailEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_empty_email,
    )

val passwordEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_empty_password,
    )