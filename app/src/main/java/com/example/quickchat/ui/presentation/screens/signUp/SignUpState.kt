package com.example.quickchat.ui.presentation.screens.signUp

import com.example.quickchat.R
import com.example.quickchat.ui.util.State.ErrorState

data class SignUpState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val cPassword: String = "",
    val phone: String = "",
    val termsAgreed: Boolean = false,
    val errorState: RegisterErrorState = RegisterErrorState(),
    val isRegisterSuccessful: Boolean = false,
    val isRegisterFailed: Boolean = false,
)

/**
 * Error state in signup holding respective
 * text field validation errors
 */
data class RegisterErrorState(
    val firstNameErrorState: ErrorState = ErrorState(),
    val lastNameErrorState: ErrorState = ErrorState(),
    val emailErrorState: ErrorState = ErrorState(),
    val passwordErrorState: ErrorState = ErrorState(),
    val cPasswordErrorState: ErrorState = ErrorState(),
    val phoneErrorState: ErrorState = ErrorState(),
    val termsErrorState: ErrorState = ErrorState(),
)

val firstNameEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_empty_fname,
    )

val lastNameEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_empty_lname,
    )

val nameInvalidErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_invalid_name,
    )

val emailEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_empty_email,
    )

val emailInvalidErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_invalid_email,
    )

val passwordEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_empty_password,
    )

val cPasswordEmptyErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_empty_password,
    )

val passwordInvalidErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_invalid_password,
    )

val cPasswordInvalidErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_invalid_c_password,
    )

val termsUncheckedErrorState =
    ErrorState(
        hasError = true,
        errorMessageStringResource = R.string.login_error_msg_unchecked_terms,
    )