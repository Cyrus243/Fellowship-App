package com.indelible.fellowship.ui.screen.maincontent

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.core.model.UserStatus
import com.indelible.fellowship.navigation.Destination
import com.indelible.fellowship.ui.component.CustomTextField
import com.indelible.fellowship.ui.component.RoundProfile
import com.indelible.fellowship.ui.component.UserInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartChatScreen(
    contacts: List<User>,
    navigateAndPopUp: (Any) -> Unit,
    onCancel: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = onCancel
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Start New Conversation",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                inputField = {
                    CustomTextField(
                        modifier = Modifier.height(46.dp),
                        value = "",
                        textStyle = MaterialTheme.typography.bodyMedium,
                        onValueChange = {},
                        placeHolder = "Search your Chat",
                    )
                },
                expanded = false,
                onExpandedChange = { },
                content = { }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.padding(horizontal = 16.dp),
                shape = RoundedCornerShape(4.dp),
                border = CardDefaults.outlinedCardBorder(),
            ) {
                Row(
                    modifier = Modifier
                        .height(28.dp)
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sort",
                        style = MaterialTheme.typography.bodySmall,
                    )

                    VerticalDivider()

                    Text(
                        text = "A-Z",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(contacts){
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navigateAndPopUp(Destination.MessageDetails("", opponentId = it.userId))
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = CardDefaults.outlinedCardBorder(),
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(text = it.name, style = MaterialTheme.typography.bodyMedium)
                            },
                            supportingContent = {
                                Text(text = it.email, style = MaterialTheme.typography.bodyMedium)
                            },
                            leadingContent = {
                                RoundProfile(
                                    modifier = Modifier.requiredSize(50.dp),
                                    imagePath = it.photoPath,
                                    isOnline = it.status == UserStatus.ONLINE
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}