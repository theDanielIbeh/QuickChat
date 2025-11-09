package com.example.quickchat.ui.presentation.screens.signUp

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quickchat.R
import com.example.quickchat.ui.presentation.screens.common.AuthTextField

@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    openAndPopUp: () -> Unit,
    modifier: Modifier = Modifier,
    application: Application = LocalContext.current.applicationContext as Application,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val state by viewModel.signUpState.collectAsStateWithLifecycle()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Sign up to get started",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        AuthTextField(
            value = state.firstName,
            onTextChange = { viewModel.onEvent(SignUpEvent.EditFirstName(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.first_name)) },
            isError = state.errorState.firstNameErrorState.hasError,
            errorText = stringResource(state.errorState.firstNameErrorState.errorMessageStringResource),
        )
        AuthTextField(
            value = state.lastName,
            onTextChange = { viewModel.onEvent(SignUpEvent.EditLastName(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.last_name)) },
            isError = state.errorState.lastNameErrorState.hasError,
            errorText = stringResource(state.errorState.lastNameErrorState.errorMessageStringResource),
        )
        AuthTextField(
            value = state.email,
            onTextChange = { viewModel.onEvent(SignUpEvent.EditEmail(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.email)) },
            type = KeyboardType.Email,
            action = ImeAction.Next,
            onKeyboardDone = {},
            isError = state.errorState.emailErrorState.hasError,
            errorText = stringResource(state.errorState.emailErrorState.errorMessageStringResource),
        )

        AuthTextField(
            value = state.password,
            onTextChange = { viewModel.onEvent(SignUpEvent.EditPassword(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.password)) },
            type = KeyboardType.Password,
            action = ImeAction.Next,
            onKeyboardDone = { },
            isError = state.errorState.passwordErrorState.hasError,
            errorText = stringResource(state.errorState.passwordErrorState.errorMessageStringResource),
        )

        AuthTextField(
            value = state.cPassword,
            onTextChange = { viewModel.onEvent(SignUpEvent.EditConfirmPassword(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.confirm_password)) },
            type = KeyboardType.Password,
            action = ImeAction.Next,
            onKeyboardDone = { viewModel.onEvent(SignUpEvent.SignUp(openAndPopUp)) },
            isError = state.errorState.cPasswordErrorState.hasError,
            errorText = stringResource(state.errorState.cPasswordErrorState.errorMessageStringResource),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (isConnected) {
                    viewModel.onEvent(SignUpEvent.SignUp(openAndPopUp))
                } else {
                    Toast.makeText(application, "No internet connection", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                "Create Account",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Already have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = { onNavigateToSignIn() }) {
                Text(
                    "Sign In",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}