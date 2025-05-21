package com.indelible.fellowship.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ProfileItem(
    title: String ="Regle de base",
    subtitle: String ="",
    onClick: () -> Unit = {}
){
    Column {
        ListItem(
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = {
                Text(
                    text = "Es tu sur d'avoir vu !",
                    style = MaterialTheme.typography.bodySmall
                )
            },
            leadingContent = {
                Image(
                    modifier = Modifier.size(56.dp),
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            trailingContent = {
                Text(text = "22:34")
            }
        )
        Divider(modifier = Modifier.padding(start = 16.dp))
    }
}


@Composable
fun SettingsItem(
    title: String,
    icon: ImageVector,
    trailing: @Composable () -> Unit = {},
    onClick: () -> Unit
){
    Column(
        modifier = Modifier.clickable {
            onClick.invoke()
        }
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )
                   },
            trailingContent = trailing,
            leadingContent = {
                //Icon(, contentDescription = null)
                Icon(imageVector = icon,
                    contentDescription = null)
            }
        )
        Divider(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
            thickness = 0.5.dp
        )
    }
}
