package com.example.quickchat.ui.presentation.screens.userName

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickchat.data.network.connectivity.ConnectivityObserver
import com.example.quickchat.domain.useCase.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsernameViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val application: Application,
    connectivityObserver: ConnectivityObserver,
) : ViewModel() {
    var state = MutableStateFlow(UsernameState())
        private set

    val isConnected = connectivityObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false,
    )

    fun onEvent(event: UsernameEvent) {
        when (event) {
            is UsernameEvent.EditPhoto -> TODO()
            is UsernameEvent.EditUsername -> {
                updateUsername(event.username)
            }

            is UsernameEvent.OnContinue -> {
                onContinue(openAndPopUp = event.popUpScreen)
            }

            is UsernameEvent.ResetState -> {
                resetState()
            }
        }
    }

    private fun updateUsername(username: String) {
        state.update { loginState -> loginState.copy(username = username) }
    }

    private fun validateInputs(): Boolean {
        val email = state.value.username.trim()

        return if (validateUsername(email)) {
            state.update { it.copy(errorState = UsernameErrorState()) }
            true
        } else {
            false
        }
    }

    fun onContinue(openAndPopUp: (String) -> Unit) {
        if (validateInputs()) {
            viewModelScope.launch {
                try {
                    openAndPopUp(state.value.username)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun validateUsername(username: String): Boolean {
        return when {
            (username.isEmpty()) -> {
                state.update {
                    it.copy(errorState = UsernameErrorState(userNameErrorState = usernameEmptyErrorState))
                }
                false
            }

            else -> {
                true
            }
        }
    }


    private fun resetState() {
        state.update { UsernameState() }
    }
}