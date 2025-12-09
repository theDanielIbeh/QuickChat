package com.example.quickchat.ui.navigation

import androidx.navigation.NavHostController

fun NavHostController.popUp() {
    navigateUp()
    popBackStack()
}

fun <T : Any> NavHostController.navigate(route: T) {
    navigate(route) { launchSingleTop = true }
}

fun <T : Any> NavHostController.navigateAndPopUp(route: T, popUp: T) {
    navigate(route) {
        launchSingleTop = true
        popUpTo(popUp) { inclusive = true }
    }
}

fun <T : Any> NavHostController.clearAndNavigate(route: T) {
    navigate(route) {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}
