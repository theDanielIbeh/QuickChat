package com.example.quickchat.ui.presentation.screens.conversationList

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListScreen(
    username: String,
    modifier: Modifier = Modifier,
) {
    val contacts = remember {
        listOf(
            Contact("Mr Yang", "Angry", "3min", true),
            Contact("Hacker Girl", "Give me your lucistack", "Yesterday", false),
            Contact("Herta", "Kukhh", "2 days ago", false),
            Contact("Dan Heng", "", "3 days ago", false),
            Contact("March", "Your pretty pink girl~", "1 week ago", false)
        )
    }
    val interactionSource = remember { MutableInteractionSource() }


    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Search bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            maxLines = 1,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary
            ),
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Text,
//                imeAction = ImeAction.Done,
//            ),
//            keyboardActions = KeyboardActions(onDone = { }),
            interactionSource = interactionSource,
            placeholder = {
                Text("Search for a chat...")
            },
            enabled = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Gray,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledIndicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            leadingIcon = {Icon(Icons.Default.Search, "Search", tint = MaterialTheme.colorScheme
                .secondary)}
        )

        // Contact list
        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(1F)
        ) {
            items(contacts) { contact ->
                ContactItem(contact) {
//                        navController.navigate("chat/${contact.name}")
                }
            }
        }
    }
}

data class Contact(
    val name: String,
    val lastMessage: String,
    val time: String,
    val isOnline: Boolean
)

@Composable
fun ContactItem(contact: Contact, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF2A2A2A), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.take(1),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6ECFA1)
                )
            }
            if (contact.isOnline) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(8.dp)
                        .background(Color(0xFF00FF00), CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contact.name,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            if (contact.lastMessage.isNotEmpty()) {
                Text(
                    text = contact.lastMessage,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Text(
            text = contact.time,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun ConversationListScreenPreview() {
    ConversationListScreen("John Doe")
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ConversationListScreenDarkPreview() {
    ConversationListScreen("John Doe")
}