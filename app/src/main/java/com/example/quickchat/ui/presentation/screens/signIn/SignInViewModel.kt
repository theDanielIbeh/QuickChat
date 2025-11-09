package com.example.quickchat.ui.presentation.screens.signIn

import android.app.Application
import android.util.Log
import android.widget.Toast
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
class SignInViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val application: Application,
    connectivityObserver: ConnectivityObserver,
) : ViewModel() {
    var state = MutableStateFlow(SignInState())
        private set

    val isConnected = connectivityObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false,
    )

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.EditEmail -> {
                updateEmail(event.email)
            }

            is SignInEvent.EditPassword -> {
                updatePassword(event.password)
            }

            is SignInEvent.ResetState -> {
                resetState()
            }

            is SignInEvent.SignIn -> {
                onSignIn(event.popUpScreen)
            }

            is SignInEvent.SignInWithGoogle -> {
                onSignInWithGoogle(event.credential, event.popUpScreen)
            }
        }
    }

    private fun updateEmail(email: String) {
        state.update { loginState -> loginState.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        state.update { loginState -> loginState.copy(password = password) }
    }

    private fun onSignIn(openAndPopUp: () -> Unit) {
        if (validateInputs()) {
            viewModelScope.launch {
                try {
                    authUseCases.signIn(state.value.email, state.value.password)
                    openAndPopUp()
                } catch (e: Exception) {
                    Toast.makeText(application, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun onSignInWithGoogle(
        credential: Credential,
        openAndPopUp: () -> Unit,
    ) {
        viewModelScope.launch {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)
                authUseCases.signInWithGoogle(googleIdTokenCredential.idToken)
                openAndPopUp()
            } else {
                Log.e("Unexpected type of credential", "Unexpected type of credential")
            }
        }
    }

    private fun validateInputs(): Boolean {
        val email = state.value.email.trim()
        val password = state.value.password.trim()

        return if (validateEmail(email) && validatePassword(password)) {
            state.update { it.copy(errorState = SignInErrorState()) }
            true
        } else {
            false
        }
    }

    private fun validateEmail(email: String): Boolean {
        return when {
            (email.isEmpty()) -> {
                state.update {
                    it.copy(errorState = SignInErrorState(emailErrorState = emailEmptyErrorState))
                }
                false
            }

            else -> {
                true
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return when {
            (password.isEmpty()) -> {
                state.update {
                    it.copy(errorState = SignInErrorState(passwordErrorState = passwordEmptyErrorState))
                }
                false
            }

            else -> {
                true
            }
        }
    }

    private fun resetState() {
        state.update { SignInState() }
    }
}