package com.example.quickchat

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quickchat.ui.navigation.NavGraph
import com.example.quickchat.ui.navigation.Routes

@Composable
fun ChatApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    // Read the current back stack entry to trigger recomposition
    navController.currentBackStackEntryAsState().value
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
        NavGraph(
            navController = navController,
            startDestination = Routes.Onboarding,
            modifier = modifier.padding(innerPadding)
        )
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