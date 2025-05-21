package com.indelible.fellowship.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AmpStories
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Call
import androidx.compose.ui.graphics.vector.ImageVector
import com.indelible.fellowship.R


enum class BottomNavItem(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    Messages(
        route = "messages",
        title = R.string.message_screen_title,
        icon = Icons.Default.ChatBubble
    ),
   Calls(
        route = "calls",
        title = R.string.call_screen_title,
        icon = Icons.Rounded.Call
    ),
    Stories(
        route = "stories",
        title = R.string.stories_screen_title,
        icon = Icons.Default.AmpStories
    ),
    Profile(
        route = "profile",
        title = R.string.profile_screen_title,
        icon = Icons.Default.Person
    )
}