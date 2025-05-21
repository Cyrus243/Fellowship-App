package com.indelible.fellowship.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ConversationDropDown(
    expanded: Boolean,
    onDismissRequest: (Boolean)-> Unit
) {

    Box(modifier = Modifier) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissRequest(false) }
        ) {
            Row {
                DropdownMenuItem(
                    text = { Text(text = "Copy")},
                    onClick = { /*TODO*/ },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Scan text")},
                    onClick = { /*TODO*/ },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                )
            }
        }

    }


}