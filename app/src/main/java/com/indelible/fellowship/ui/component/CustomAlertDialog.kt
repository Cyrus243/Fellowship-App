package com.indelible.fellowship.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun CustomAlertDialog(
    onDismissRequest: ()-> Unit,
    onConfirm: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(
            text = "Delete this messsage ?",
            style = MaterialTheme.typography.titleMedium
        ) },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismissRequest()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        },
        text = { Text(text = "This action cannot be undone.") }
    )
}
