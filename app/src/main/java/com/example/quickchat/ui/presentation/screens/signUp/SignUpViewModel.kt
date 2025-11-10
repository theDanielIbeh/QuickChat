package com.example.quickchat.ui.presentation.screens.signUp

import android.util.Log
import android.util.Patterns
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickchat.data.network.connectivity.ConnectivityObserver
import com.example.quickchat.domain.useCase.AuthUseCases
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val authUseCases: AuthUseCases,
) : ViewModel() {
    var signUpState = MutableStateFlow(SignUpState())
        private set

    val isConnected = connectivityObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false,
    )


    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EditConfirmPassword -> {
                updateConfirmPassword(event.password)
            }

            is SignUpEvent.EditEmail -> {
                updateEmail(event.email)
            }

            is SignUpEvent.EditPassword -> {
                updatePassword(event.password)
            }

            is SignUpEvent.EditPhone -> {
                updatePhone(event.phone)
            }

            is SignUpEvent.ResetState -> {
                signUpState.update { SignUpState() }
            }

            is SignUpEvent.SignUp -> {
                register(event.popUpScreen)
            }

            is SignUpEvent.SignUpWithGoogle -> {
                onSignUpWithGoogle(event.credential, event.popUpScreen)
            }
        }
    }

    private fun updateEmail(email: String) {
        signUpState.update { loginState -> loginState.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        signUpState.update { loginState -> loginState.copy(password = password) }
    }

    private fun updateConfirmPassword(password: String) {
        signUpState.update { loginState -> loginState.copy(cPassword = password) }
    }

    private fun updatePhone(phone: String) {
        signUpState.update { loginState -> loginState.copy(phone = phone) }
    }

    fun updateTermsAgreed() {
        signUpState.update { loginState ->
            val termsAgreed = loginState.termsAgreed
            loginState.copy(termsAgreed = !termsAgreed)
        }
    }

    private fun register(popUpScreen: () -> Unit) {
        viewModelScope.launch {
            try {
                val isInputsValidated = validateInputs()
                Log.d("Register", isInputsValidated.toString())
                if (isInputsValidated) {
                    authUseCases.signUp(
                        signUpState.value.email,
                        signUpState.value.password,
                        signUpState.value.firstName,
                        signUpState.value.lastName,
                    )
                    popUpScreen()
                }
            } catch (e: Exception) {
                throw Exception(e)
            }
        }
    }

    private fun validateInputs(): Boolean {
        val email = signUpState.value.email.trim()
        val password = signUpState.value.password.trim()
        val cPassword = signUpState.value.cPassword.trim()
        val terms = signUpState.value.termsAgreed

        Log.d("Register", signUpState.value.errorState.toString())
        return if (
            validateEmail(email) &&
            validatePassword(password) &&
            validateConfirmPassword(cPassword) &&
            validateTerms(terms)
        ) {
            signUpState.update { it.copy(errorState = RegisterErrorState()) }
            Log.d("Register", signUpState.value.errorState.toString())
            true
        } else {
            false
        }
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            (email.isEmpty()) -> {
                signUpState.update {
                    it.copy(
                        errorState =
                            RegisterErrorState(
                                emailErrorState = emailEmptyErrorState,
                            ),
                    )
                }
                false
            }

            (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) -> {
                signUpState.update {
                    it.copy(
                        errorState =
                            RegisterErrorState(
                                emailErrorState = emailInvalidErrorState,
                            ),
                    )
                }
                false
            }

            else -> {
                true
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        val pattern =
            "^(?=(.*[A-Z]){2,})(?=(.*[a-z]){2,})(?=(.*\\d){2,})(?=(.*[^a-zA-Z\\d]){2,}).{8,}\$"
        return when {
            (password.isEmpty()) -> {
                signUpState.update {
                    it.copy(
                        errorState =
                            RegisterErrorState(
                                passwordErrorState = passwordEmptyErrorState,
                            ),
                    )
                }
                false
            }

            (!Regex(pattern).matches(password)) -> {
                signUpState.update {
                    it.copy(
                        errorState =
                            RegisterErrorState(
                                passwordErrorState = passwordInvalidErrorState,
                            ),
                    )
                }
                false
            }

            else -> {
                true
            }
        }
    }

    private fun validateConfirmPassword(confirmPassword: String): Boolean {
        return when {
            (confirmPassword.isEmpty()) -> {
                signUpState.update {
                    it.copy(
                        errorState =
                            RegisterErrorState(
                                cPasswordErrorState = cPasswordEmptyErrorState,
                            ),
                    )
                }
                false
            }

            (confirmPassword != signUpState.value.password) -> {
                signUpState.update {
                    it.copy(
                        errorState =
                            RegisterErrorState(
                                cPasswordErrorState = cPasswordInvalidErrorState,
                            ),
                    )
                }
                false
            }

            else -> {
                true
            }
        }
    }

    private fun validateTerms(terms: Boolean): Boolean {
        return when {
            (!terms) -> {
                signUpState.update {
                    it.copy(
                        errorState =
                            RegisterErrorState(
                                termsErrorState = termsUncheckedErrorState,
                            ),
                    )
                }
                false
            }

            else -> {
                true
            }
        }
    }

    private fun onSignUpWithGoogle(
        credential: Credential,
        openAndPopUp: () -> Unit,
    ) {
        viewModelScope.launch {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)
                authUseCases.signUpWithGoogle(googleIdTokenCredential.idToken)
                openAndPopUp()
            } else {
                Log.e("Unexpected type of credential", "Unexpected type of credential")
            }
        }
    }
}