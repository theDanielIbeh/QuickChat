package com.example.quickchat.ui.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.quickchat.R
import com.example.quickchat.ui.navigation.Routes
import com.example.quickchat.ui.presentation.screens.conversationList.ConversationListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavItem = BottomNavItem.Home,
) {
    when (selectedItem) {
        is BottomNavItem.Home -> {
            ConversationListScreen(
                "Daniel Ibeh",
                modifier = modifier.fillMaxSize()
            )
        }

        else -> {}
    }

}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    selectedItem: BottomNavItem,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

/**
 * Represents a sealed class hierarchy for bottom navigation destinations.
 *
 * @property route The navigation [Routes] object for the screen.
 * @property title The label shown under the icon.
 * @property icon The icon shown in the bottom navigation item.
 */
sealed class BottomNavItem(
    val route: Routes,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Routes.Home,
        title = "Home",
        icon = Icons.Default.Home
    )

    object Profile : BottomNavItem(
        route = Routes.Profile,
        title = "Profile",
        icon = Icons.Default.MoreVert
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
