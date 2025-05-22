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
    val screen: Any,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    Messages(
        route = Destination.Messages.toString(),
        screen = Destination.Messages,
        title = R.string.message_screen_title,
        icon = Icons.Default.ChatBubble
    ),
   Calls(
       route = Destination.Messages.toString(),
       screen = Destination.Calls,
       title = R.string.call_screen_title,
       icon = Icons.Rounded.Call
    ),
    Stories(
        route = Destination.Stories.toString(),
        screen = Destination.Stories,
        title = R.string.stories_screen_title,
        icon = Icons.Default.AmpStories
    ),
    Profile(
        route = Destination.Profile.toString(),
        screen = Destination.Profile,
        title = R.string.profile_screen_title,
        icon = Icons.Default.Person
    )
}