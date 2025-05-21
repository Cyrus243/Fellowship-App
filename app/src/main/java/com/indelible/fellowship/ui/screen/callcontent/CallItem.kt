package com.indelible.fellowship.ui.screen.callcontent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indelible.fellowship.ui.component.RoundProfile

@Preview(showBackground = true)
@Composable
fun CallItem(){

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundProfile(Modifier.requiredSize(45.dp), "", false)

        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column{
                    Text(
                        text = "Arthur",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Row {
                        Icon(imageVector = Icons.Default.CallMade,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Gray,
                            contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Today at 15:07",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .requiredSize(35.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Call,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null)
                    }
                }

            }
        }
    }
}