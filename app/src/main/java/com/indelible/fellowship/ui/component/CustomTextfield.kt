package com.indelible.fellowship.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextField(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "",
    textStyle: TextStyle,
    isSingleLine: Boolean = true
){
    BasicTextField(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        value = text,
        onValueChange = { onValueChange(it) },
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) Text(
                        placeholderText,
                        style = LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            fontSize = textStyle.fontSize
                        )
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}

@Preview()
@Composable
fun TextFieldPreview(){
    CustomTextField(
        text = "",
        textStyle = MaterialTheme.typography.bodyMedium,
        leadingIcon = { Icon(
            imageVector = Icons.Default.Search,
            modifier = Modifier.padding(4.dp),
            contentDescription = null)
        },
        placeholderText = "Search",
        onValueChange = {}
    )
}