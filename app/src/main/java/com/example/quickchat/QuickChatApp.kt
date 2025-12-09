package com.example.quickchat

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quickchat.ui.navigation.NavGraph
import com.example.quickchat.ui.navigation.Routes
import com.example.quickchat.ui.presentation.screens.home.BottomNavItem
import com.example.quickchat.ui.presentation.screens.home.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickChatApp(
    modifier: Modifier = Modifier,
    viewModel: QuickChatViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {

    // Read the current back stack entry to trigger recomposition
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route
    val isHome = currentRoute == Routes.Home.javaClass.canonicalName
    val canNavigateBack = navController.previousBackStackEntry != null

    val items = listOf(BottomNavItem.Home, BottomNavItem.Profile)
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val selectedItem = items[selectedItemIndex]


    Log.d("ChatApp", "currentBackStackEntry: $currentBackStackEntry")

    Scaffold(
        topBar = {
            AppBar(
                isHome = isHome,
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            if (isHome)
                BottomNavigationBar(items, selectedItem, { selectedItemIndex = items.indexOf(it) })
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            NavGraph(
                selectedItem = selectedItem,
                navController = navController,
                startDestination = /*if (currentUser == null) Routes.Onboarding else Routes.Home*/ Routes.Home,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    isHome: Boolean = false,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Add this debug log
    Log.d("AppBar", "canNavigateBack: $canNavigateBack")
    TopAppBar(
        title = {
            if (isHome) {
                Text(
                    stringResource(R.string.app_name),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        actions = {
            if (isHome) {
                IconButton(onClick = { /*navController.navigate("settings")*/ }) {
                    Icon(Icons.Default.MoreVert, "Menu", tint = Color.White)
                }
            }
        },
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

@Preview
@Composable
fun QuickChatAppPreview() {
    QuickChatApp()
}