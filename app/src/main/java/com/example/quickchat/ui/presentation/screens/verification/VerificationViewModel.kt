package com.example.quickchat.ui.presentation.screens.verification

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickchat.data.auth.FirebaseAuthRepository
import com.example.quickchat.data.auth.PhoneAuthResult
import com.example.quickchat.ui.util.Constants.VERIFICATION_TIMEOUT_SECONDS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    var state = MutableStateFlow(VerificationState())
        private set

    var ticks = MutableStateFlow(VERIFICATION_TIMEOUT_SECONDS)
        private set

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = flow {
            for (i in VERIFICATION_TIMEOUT_SECONDS downTo 0) {
                emit(i)
                delay(1000)
            }
        }.onEach { tick ->
            state.update {
                it.copy(
                    isTimerRunning = tick > 0
                )
            }
            ticks.update { tick }
        }.launchIn(viewModelScope)
    }

    private fun startPhoneNumberVerification(phoneNumber: String, activity: Activity) {
        firebaseAuthRepository.startPhoneNumberVerification(phoneNumber, activity)
            .onEach { result ->
                when (result) {
                    is PhoneAuthResult.CodeSent -> {
                        state.update {
                            it.copy(
                                verificationId = result.verificationId,
                                resendToken = result.token
                            )
                        }
                        startTimer()
                    }

                    is PhoneAuthResult.VerificationCompleted -> {
                        // Auto-verification, sign in with credential
                        state.value.verificationId?.let {
                            firebaseAuthRepository.signInWithPhoneAuthCredential(
                                it,
                                state.value.code.joinToString()
                            )
                        }
                    }

                    is PhoneAuthResult.VerificationFailed -> {
                        // Handle verification failure
                        state.update {
                            it.copy(
                                error = result.exception.message
                            )
                        }
                        Log.d(
                            "VerificationViewModel",
                            "Verification failed: ${result.exception.message}"
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun resendVerificationCode(phoneNumber: String, activity: Activity) {
        val resendToken = state.value.resendToken
        if (resendToken != null) {
            firebaseAuthRepository.resendVerificationCode(phoneNumber, activity, resendToken)
                .onEach { result ->
                    when (result) {
                        is PhoneAuthResult.CodeSent -> {
                            state.update {
                                it.copy(
                                    verificationId = result.verificationId,
                                    resendToken = result.token
                                )
                            }
                            startTimer()
                        }

                        is PhoneAuthResult.VerificationCompleted -> {
                            // Auto-verification, sign in with credential
                            state.value.verificationId?.let {
                                firebaseAuthRepository.signInWithPhoneAuthCredential(
                                    it,
                                    state.value.code.joinToString("")
                                )
                            }
                        }

                        is PhoneAuthResult.VerificationFailed -> {
                            // Handle verification failure
                            state.update {
                                it.copy(
                                    error = result.exception.message
                                )
                            }
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun signInWithSmsCode() {
        val state = state.value
        Log.d("VerificationViewModel", state.code.joinToString(""))
        val code = state.code.joinToString("")
        val verificationId = state.verificationId
        if (verificationId != null) {
            viewModelScope.launch {
                firebaseAuthRepository.signInWithPhoneAuthCredential(verificationId, code)
            }
        }
    }

    fun onAction(action: OtpAction) {
        when (action) {
            is OtpAction.OnChangeFieldFocus -> {
                state.update {
                    it.copy(
                        focusedIndex = action.index
                    )
                }
            }

            is OtpAction.OnEnterNumber -> enterNumber(number = action.number, index = action.index)

            is OtpAction.StartPhoneNumberVerification -> {
                startPhoneNumberVerification(action.phoneNumber, action.activity)
            }

            is OtpAction.OnResendVerificationCode -> {
                resendVerificationCode(action.phoneNumber, action.activity)
            }

            is OtpAction.OnVerifyPhoneNumber -> {
                signInWithSmsCode()
            }

            OtpAction.OnKeyboardBackspace -> {
                val previousFocusedIndex = getPreviousFocusedIndex(state.value.focusedIndex)
                state.update {
                    it.copy(
                        code = it.code.mapIndexed { index, currentNumber ->
                            if (index == previousFocusedIndex) {
                                null
                            } else {
                                currentNumber
                            }
                        },
                        focusedIndex = previousFocusedIndex
                    )
                }
            }
        }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? =
        currentIndex?.minus(1)?.coerceAtLeast(minimumValue = 0)


    private fun getFirstEmptyIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        return code.subList(currentFocusedIndex + 1, code.size).indexOfFirst { it == null }
            .takeIf { it != -1 } ?: currentFocusedIndex
    }

    private fun getNextFocusedIndex(currentFocusedIndex: Int?, currentCode: List<Int?>): Int? {
        return if (currentFocusedIndex == null) {
            null
        } else {
            getFirstEmptyIndexAfterFocusedIndex(
                code = currentCode,
                currentFocusedIndex = currentFocusedIndex
            )
                .coerceAtMost(currentCode.lastIndex)
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if (currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        state.update {
            it.copy(
                code = newCode,
                focusedIndex = if (wasNumberRemoved || it.code.getOrNull(index) != null) {
                    it.focusedIndex
                } else {
                    getNextFocusedIndex(
                        currentFocusedIndex = it.focusedIndex,
                        currentCode = newCode
                    )
                }
            )
        }
    }

    fun resetState(activity: Activity) {
        state.update {
            VerificationState()
        }
        startTimer()
        state.value.phone?.let {
            onAction(
                OtpAction.OnResendVerificationCode(
                    phoneNumber = it,
                    activity = activity
                )
            )
        }
    }
}