package com.example.quickchat.ui.presentation.screens.signIn

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
fun SignInScreen(
    application: Application = LocalContext.current.applicationContext as Application,
    onNavigateToSignUp: () -> Unit,
    openAndPopUp: () -> Unit,
    modifier: Modifier = Modifier,
    loginViewModel: SignInViewModel = hiltViewModel<SignInViewModel>()
) {
    val loginState by loginViewModel.state.collectAsStateWithLifecycle()
    val isConnected by loginViewModel.isConnected.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Welcome back",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Sign in to continue",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        AuthTextField(
            value = loginState.email,
            onTextChange = {
                loginViewModel.onEvent(SignInEvent.EditEmail(it))
//                loginViewModel.validateEmail(it)
            },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.email)) },
            type = KeyboardType.Email,
            action = ImeAction.Next,
            isError = loginState.errorState.emailErrorState.hasError,
            errorText = stringResource(loginState.errorState.emailErrorState.errorMessageStringResource),
        )

        AuthTextField(
            value = loginState.password,
            onTextChange = {
                loginViewModel.onEvent(SignInEvent.EditPassword(it))
//                loginViewModel.validatePassword(it)
            },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.password)) },
            type = KeyboardType.Password,
            action = ImeAction.Done,
            onKeyboardDone = { loginViewModel.onEvent(SignInEvent.SignIn(openAndPopUp)) },
            isError = loginState.errorState.passwordErrorState.hasError,
            errorText = stringResource(loginState.errorState.passwordErrorState.errorMessageStringResource),
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = { /* Handle forgot password */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                "Forgot password?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // TODO: Firebase authentication
                if (isConnected) {
                    loginViewModel.onEvent(SignInEvent.SignIn(openAndPopUp))
                } else {
                    Toast.makeText(application, "No internet connection", Toast.LENGTH_LONG)
                        .show()
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
                "Sign In",
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
                "Don't have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = { onNavigateToSignUp() }) {
                Text(
                    "Sign Up",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}