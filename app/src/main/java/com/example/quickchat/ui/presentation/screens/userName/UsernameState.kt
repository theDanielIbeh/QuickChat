package com.example.quickchat.ui.presentation.screens.userName

import com.example.quickchat.R
import com.example.quickchat.ui.util.State.ErrorState


/**
 * Error state in login holding respective
 * text field validation errors
 */
data class UsernameErrorState(
    val userNameErrorState: ErrorState = ErrorState(),
)

/**
 * Login State holding ui input values
 */
data class UsernameState(
    val username: String = "",
    val errorState: UsernameErrorState = UsernameErrorState(),
)

val usernameEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_empty_email,
    )