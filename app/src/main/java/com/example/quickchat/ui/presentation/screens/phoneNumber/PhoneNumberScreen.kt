package com.example.quickchat.ui.presentation.screens.phoneNumber

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quickchat.ui.presentation.screens.common.PhoneNumberTextField
import com.example.quickchat.ui.util.PhoneNumberValidator
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState

@Composable
fun PhoneNumberScreen(
    phone: String,
    onPhoneEntered: (String) -> Unit,
    onContinueClicked: (String) -> Unit,  // Passes validated E164 phone number
    modifier: Modifier = Modifier
) {
    // State for country code picker
    val countryCodePickerState = rememberKomposeCountryCodePickerState(
        defaultCountryCode = "GB"
    )

    // State for error message
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // Clear error when user types
    LaunchedEffect(phone) {
        if (phone.isNotEmpty()) {
            errorMessage = null
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Enter your phone number",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We'll send you a verification code",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        PhoneNumberTextField(
            phone = phone,
            onPhoneEntered = onPhoneEntered,
            errorMessage = errorMessage,
            countryCodePickerState = countryCodePickerState
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Validate the phone number using PhoneNumberValidator
                val result = PhoneNumberValidator.validateAndFormat(
                    phoneNumber = phone,
                    state = countryCodePickerState
                )


                if (result.isValid) {
                    // Pass the E164 formatted number (e.g., +447911123456)
                    result.formattedE164?.let { onContinueClicked(it) }
                } else {
                    // Show error message
                    errorMessage = result.errorMessage
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = phone.isNotEmpty()
        ) {
            Text(
                "Continue",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
fun PhoneNumberScreenPreview() {
    PhoneNumberScreen(
        onPhoneEntered = {},
        phone = "",
        onContinueClicked = { },
    )
}

