package com.example.quickchat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickchat.ui.presentation.screens.onboarding.OnboardingScreen
import com.example.quickchat.ui.presentation.screens.signIn.SignInScreen
import com.example.quickchat.ui.presentation.screens.signUp.SignUpScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Routes = Routes.Onboarding
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
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
                    navController.navigateAndPopUp(Routes.SignUp, Routes.Onboarding)
                }
            )
        }
        composable<Routes.SignUp> {
            SignUpScreen(
                onNavigateToSignIn = {
                    navController.clearAndNavigate(Routes.SignIn)
                },
                openAndPopUp = {
                    navController.clearAndNavigate(Routes.Home)
                },
            )
        }
        composable<Routes.SignIn> {
            SignInScreen(
                onNavigateToSignUp = {
                    navController.clearAndNavigate(Routes.SignUp)
                },
                openAndPopUp = {
                    navController.clearAndNavigate(Routes.Home)
                },
            )
        }
//            composable<Routes.Phone> {
//                PhoneNumberScreen(
//                    phone = phoneNumber.value,
//                    onPhoneEntered = { phone ->
//                        phoneNumber.value = phone
//                    }, onContinueClicked = { phone ->
//                        navController.navigate(Routes.Otp(phone))
//                    }
//                )
//            }
//            composable<Routes.Otp> { backStackEntry ->
//                val viewModel = hiltViewModel<VerificationViewModel>()
//                val state by viewModel.state.collectAsStateWithLifecycle()
//                val ticks by viewModel.ticks.collectAsStateWithLifecycle()
//                val focusRequesters = remember { List(size = VERIFICATION_CODE_LENGTH) {
//                    FocusRequester()
//                } }
//                val phoneNumber = backStackEntry.arguments?.getString(PHONE) ?: ""
//                VerificationScreen(
//                    phone = phoneNumber,
//                    onContinue = {
////                        navController.navigate(Routes.Profile)
//                    },
//                    state = state,
//                    ticks = ticks,
//                    focusRequesters = focusRequesters,
//                    onAction = { action ->
//                        when (action) {
//                            is OtpAction.OnEnterNumber -> {
//                                if (action.number != null) {
//                                    focusRequesters[action.number].freeFocus()
//                                }
//                            }
//
//                            else -> Unit
//                        }
//                        viewModel.onAction(action)
//                    },
//                    modifier = Modifier,
//                    resetState = {
//                        viewModel::resetState
//                    }
//                )
//            }

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
