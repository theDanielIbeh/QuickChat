package com.example.quickchat.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quickchat.R
import com.example.quickchat.ui.presentation.screens.onboarding.OnboardingScreen
import com.example.quickchat.ui.presentation.screens.phoneNumber.PhoneNumberScreen
import com.example.quickchat.ui.presentation.screens.verification.OtpAction
import com.example.quickchat.ui.presentation.screens.verification.VerificationScreen
import com.example.quickchat.ui.presentation.screens.verification.VerificationViewModel
import com.example.quickchat.ui.util.Constants.PHONE
import com.example.quickchat.ui.util.Constants.VERIFICATION_CODE_LENGTH

@Composable
fun ChatApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val username = retain { mutableStateOf("") }
    val phoneNumber = retain { mutableStateOf("") }

    // Read the current back stack entry to trigger recomposition
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val canNavigateBack = navController.previousBackStackEntry != null

    Scaffold(
        topBar = {
            AppBar(
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Onboarding,
            modifier = modifier.padding(innerPadding)
        ) {
//        composable<Routes.Splash> {
//            SplashScreen(
//                onSplashFinished = {
//                    navController.navigate(Routes.Onboarding) {
//                        popUpTo(Routes.Splash) {
//                            inclusive = true
//                        }
//                    }
//                }
//            )
//        }
            composable<Routes.Onboarding> {
                OnboardingScreen(
                    onGetStartedClicked = {
                        navController.navigate(Routes.Phone) {
                            launchSingleTop = true
                            popUpTo(Routes.Onboarding) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Routes.Phone> {
                PhoneNumberScreen(
                    phone = phoneNumber.value,
                    onPhoneEntered = { phone ->
                        phoneNumber.value = phone
                    }, onContinueClicked = { phone ->
                        navController.navigate(Routes.Otp(phone))
                    }
                )
            }
            composable<Routes.Otp> { backStackEntry ->
                val viewModel = hiltViewModel<VerificationViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                val ticks by viewModel.ticks.collectAsStateWithLifecycle()
                val focusRequesters = remember { List(size = VERIFICATION_CODE_LENGTH) {
                    FocusRequester()
                } }
                val phoneNumber = backStackEntry.arguments?.getString(PHONE) ?: ""
                VerificationScreen(
                    phone = phoneNumber,
                    onContinue = {
//                        navController.navigate(Routes.Profile)
                    },
                    state = state,
                    ticks = ticks,
                    focusRequesters = focusRequesters,
                    onAction = { action ->
                        when (action) {
                            is OtpAction.OnEnterNumber -> {
                                if (action.number != null) {
                                    focusRequesters[action.number].freeFocus()
                                }
                            }

                            else -> Unit
                        }
                        viewModel.onAction(action)
                    },
                    modifier = Modifier,
                    resetState = {
                        viewModel::resetState
                    }
                )
            }
//        composable<Routes.Username> {
//            UsernameScreen(navController) { name ->
//                username.value = name
//            }
//        }
//        composable<Routes.Profile> {
//            ProfileScreen(navController, username.value, phoneNumber.value)
//        }
//        composable<Routes.ConversationList> {
//            ConversationListScreen(navController, username.value, phoneNumber.value)
//        }
//        composable<Routes.Conversation> {
//            ConversationScreen(navController, username.value, phoneNumber.value)
//        }
        }
    }
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Add this debug log
    Log.d("AppBar", "canNavigateBack: $canNavigateBack")
    TopAppBar(
        title = { },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    )
}