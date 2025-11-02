package com.example.quickchat.ui.presentation.screens.verification

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun VerificationScreen(
    phone: String,
    state: VerificationState,
    ticks: Long,
    focusRequesters: List<FocusRequester>,
    resetState: (activity: Activity) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    onAction: (OtpAction) -> Unit = {},
    context: Context = LocalContext.current
) {
    val activity = context as Activity
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(phone) {
        onAction(OtpAction.StartPhoneNumberVerification(phoneNumber = phone, activity = activity))
    }

    LaunchedEffect(state.error) {
        if (state.error != null) {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
            resetState(activity)
        }
    }

    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let {
            focusRequesters.getOrNull(it)?.requestFocus()
        }
    }


    LaunchedEffect(state.code, keyboardController) {
        val allNumbersEntered = state.code.none { it == null }
        if (allNumbersEntered) {
            focusRequesters.forEach { it.freeFocus() }
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(120.dp))

        Text(
            text = "Enter your verification code",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "We have sent a verification code to\n$phone",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(height = 32.dp))
        // Visual display of code boxes
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            state.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number,
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { isFocused ->
                        if (isFocused) {
                            onAction(OtpAction.OnChangeFieldFocus(index = index))
                        }
                    },
                    onValueChanged = { newNumber ->
                        onAction(OtpAction.OnEnterNumber(number = newNumber, index = index))
                    },
                    onKeyboardBackspace = {
                        onAction(OtpAction.OnKeyboardBackspace)
                    },
                    modifier = Modifier
                        .weight(weight = 1f)
                        .aspectRatio(ratio = 1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isTimerRunning) {
            Text(
                text = "Resend code in ${ticks}s",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            TextButton(
                onClick = { resetState(activity) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Resend code", color = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onContinue()
                onAction(
                    OtpAction.OnVerifyPhoneNumber(
                        code = state.code.joinToString(separator = "")
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = state.isValid ?: false
        ) {
            Text(
                "Continue",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}