package com.indelible.fellowship.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier.height(46.dp),
    value: String,
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
    placeHolder: String = "",
    enabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onFocusStateChanged: (state: Boolean) -> Unit = {}
){
    var focusState by remember {
        mutableStateOf(false)
    }
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusEvent { state ->
                focusState = state.hasFocus
                onFocusStateChanged(state.hasFocus)
            },
        enabled = enabled,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        value = value,
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        decorationBox = { innerTextField ->
            OutlinedCard(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize()){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .align(Alignment.CenterStart)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                leadingIcon()
                                if (value.isEmpty()){
                                    Text(
                                        text = placeHolder,
                                        style = textStyle.copy(
                                            color = LocalContentColor.current.copy(alpha = .6f)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                            trailingIcon()
                        }
                        innerTextField()
                    }
                }
            }

        }
    )
}

@Preview()
@Composable
fun TextFieldPreview(){
    CustomTextField(
        value = "",
        textStyle = MaterialTheme.typography.bodyMedium,
        leadingIcon = { Icon(
            imageVector = Icons.Default.Search,
            modifier = Modifier.padding(4.dp),
            contentDescription = null)
        },
        placeHolder = "Search",
        onValueChange = {}
    )
}