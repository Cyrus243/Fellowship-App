package com.indelible.fellowship.ui.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.indelible.fellowship.ConversationState
import com.indelible.fellowship.core.model.Message
import kotlinx.coroutines.delay

@Composable
fun UserInput(
    modifier: Modifier = Modifier,
    onMessageSent: () -> Unit,
    uiState: ConversationState,
    onTextChanged: (String) -> Unit,
    resetScroll: () -> Unit,
    uploadMedia: (Uri) -> Unit,
    expanded: Boolean,
    onExpand:(Boolean) -> Unit,
    isMessageSelected: Boolean,
    message: Message,
    onDismiss: () -> Unit,
    onRecordPress: (Boolean) -> Unit,
) {

    var textFieldFocusState by remember{
        mutableStateOf(false)
    }

    Surface{
        Column(modifier = modifier) {
            ConversationDropDown(
                expanded = expanded,
                onDismissRequest = onExpand
            )
            UserInputText(
                onTextChanged =  onTextChanged,
                textFieldValue = uiState.message,
                keyboardShown = false,
                onTextFieldFocused = { focused ->
                    if (focused)
                        resetScroll()
                    textFieldFocusState = focused
                },
                focusState = textFieldFocusState,
                onMessageSent = {
                    onMessageSent()
                    resetScroll()
                },
                uploadMedia = uploadMedia,
                isMessageSelected = isMessageSelected,
                message = message,
                onDismiss = onDismiss,
                onRecordPress = onRecordPress,
                counter = uiState.counter
            )
        }
    }
}

@Composable
fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (String) -> Unit,
    textFieldValue: String,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean,
    onMessageSent: () -> Unit,
    uploadMedia: (Uri) -> Unit,
    isMessageSelected: Boolean,
    message: Message,
    onDismiss: () -> Unit,
    onRecordPress: (Boolean) -> Unit,
    counter: Int
){

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    var temp by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = isPressed){
        while (isPressed){
            delay(1000)
            temp++
        }
    }


    val infiniteTransition = rememberInfiniteTransition()
    val size by infiniteTransition.animateValue(
        initialValue = 20.dp,
        targetValue = 14.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )


    var lastFocusState by remember{ mutableStateOf(false) }

    Column() {

        AnimatedVisibility(visible = isMessageSelected){
            Divider(thickness = 1.dp)
            ReplyBox(message, onDismiss)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Surface(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 2.dp,
            ){

                Row(
                    modifier = Modifier
                        .heightIn(min = 48.dp, max = 100.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    val trailingSize by animateDpAsState(
                        targetValue = if (isPressed) size else 24.dp
                    )

                    IconButton(
                        modifier = Modifier.size(34.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        val trailingIcon = if (isPressed) Icons.Filled.FiberManualRecord else
                            Icons.Outlined.Mood
                        val trailingIconTint = if (isPressed) Color.Red else
                            MaterialTheme.colorScheme.secondary

                        Icon(
                            modifier = Modifier.size(trailingSize),
                            imageVector = trailingIcon,
                            tint = trailingIconTint,
                            contentDescription = null
                        )
                    }

                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(end = 8.dp)
                            .onFocusChanged { state ->
                                if (lastFocusState != state.isFocused) {
                                    onTextFieldFocused(state.isFocused)
                                }
                                lastFocusState = state.isFocused
                            },
                        value = textFieldValue,
                        onValueChange = { onTextChanged(it) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            autoCorrect = true,
                            keyboardType = keyboardType,
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(onSend = { onMessageSent() }),
                        cursorBrush = SolidColor(LocalContentColor.current),
                        textStyle = MaterialTheme.typography.bodyMedium
                            .copy(color = LocalContentColor.current),
                        decorationBox = { innerTextField ->
                            Box() {
                                DecorationBox(
                                    isRecording = isPressed,
                                    textFieldValue = textFieldValue,
                                    focusState = focusState,
                                    count = temp
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }
            if (textFieldValue.isEmpty()){
                InputSelector(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    uploadMedia = uploadMedia,
                    isRecording = isPressed,
                    interactionSource = interactionSource,
                    onRecordPress = { onRecordPress(isPressed) },
                    transition = infiniteTransition
                )
            }else{
                TextButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .height(34.dp)
                        .padding(end = 16.dp),
                    onClick =  onMessageSent
                ) {
                    Text(
                        text = "Send",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BoxScope.DecorationBox(
    isRecording: Boolean,
    textFieldValue: String,
    focusState: Boolean,
    count: Int
){

    val disableContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    AnimatedVisibility (
        isRecording,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500))
    ){
        Row(
            modifier = Modifier
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "00:"+ String.format("%02d", count))

            Spacer(modifier = Modifier.width(24.dp))

            Icon(
                imageVector = Icons.Default.ArrowLeft,
                contentDescription = null
            )
            Text(
                text = "Slide to cancel",
                style = LocalTextStyle.current.copy(color = disableContentColor)
            )
        }
    }
    AnimatedVisibility (
        textFieldValue.isEmpty() && !focusState && !isRecording,
        exit = fadeOut(animationSpec = tween(500))
    ){
        Text(
            modifier = Modifier,
            text = "Message",
            style = LocalTextStyle.current.copy(color = disableContentColor)
        )
    }
}


@Composable
fun ReplyBox(
    message: Message,
    onDismiss: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .padding(end = 16.dp, start = 16.dp, top = 16.dp)
        ) {

            Text(
                text = "Answer to ${message.author}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = LocalContentColor.current.copy(alpha = 0.3f)
                )
            )
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodySmall
            )

        }

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }
    }

}


@Composable
fun InputSelector(
    modifier: Modifier = Modifier,
    uploadMedia: (Uri) -> Unit,
    isRecording: Boolean,
    interactionSource: MutableInteractionSource,
    onRecordPress: ()->Unit,
    transition: InfiniteTransition
){

    val rippleScale by transition.animateValue(
        initialValue = 0f,
        targetValue = 2f,
        typeConverter = Float.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val recordButtonScale by transition.animateValue(
        initialValue = 1f,
        targetValue = 0.8f,
        typeConverter = Float.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val rowPadding by animateDpAsState(targetValue = if (isRecording) 10.dp else 20.dp)

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ){ uri ->
        if (uri != null){
            uploadMedia(uri)
        }

    }
    Row(
        modifier = modifier.padding(rowPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(visible = !isRecording) {
            Row {
                IconButton(
                    modifier = Modifier.size(34.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddCircle,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )
                }

                IconButton(
                    modifier = Modifier.size(34.dp),
                    onClick = {
                        pickMedia.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Image,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        val recordButtonBackground by animateColorAsState(
            targetValue = if (isRecording )MaterialTheme.colorScheme.secondary else
            Color.Transparent
        )

        val recordButtonColor by animateColorAsState(
            targetValue = if (isRecording) MaterialTheme.colorScheme.onSecondary else
                MaterialTheme.colorScheme.secondary
        )
        val recordButtonSize by animateDpAsState(
            targetValue = if (isRecording) 54.dp else 34.dp
        )


        Box(contentAlignment = Alignment.Center) {

            RippleCircle(
                scale = if (isRecording) rippleScale else 0f,
                size = recordButtonSize
            )

            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .scale(
                        if (isRecording) recordButtonScale else 1f
                    )
                    .size(recordButtonSize)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {

                            }
                        )

                    }
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {}
                    )
                ,
                color = recordButtonBackground
            ) {
                Box {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp),
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = null,
                        tint = recordButtonColor
                    )
                }

            }
        }

    }
}


@Composable
fun RippleCircle(scale: Float, size: Dp){
    Box(
        modifier = Modifier
            .scale(scale)
            .size(size)
            .clip(CircleShape)
            .background(Color.Gray.copy(alpha = 0.1f))
    )
}
@Composable
fun UserInputSelector(
    onSelectorChange: () -> Unit,
    sendMessageEnabled: Boolean,
    onMessageSent: () -> Unit,
    modifier: Modifier = Modifier,
    uploadMedia: (Uri)-> Unit
) {

    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ){ uri ->
        if (uri != null){
            uploadMedia(uri)
        }

    }

    Row(
        modifier = modifier
            .height(72.dp)
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputSelectorButton(
            onClick = { },
            icon = Icons.Outlined.AlternateEmail,
            description = "",
            selected = false
        )
        InputSelectorButton(
            onClick = {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            icon = Icons.Outlined.InsertPhoto,
            description = "",
            selected = false
        )
        InputSelectorButton(
            onClick = { },
            icon = Icons.Outlined.Place,
            description = "",
            selected = false
        )
        InputSelectorButton(
            onClick = { },
            icon = Icons.Outlined.Duo,
            description = "",
            selected = false
        )

        val border = if (!sendMessageEnabled) {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
            )
        } else {
            null
        }
        Spacer(modifier = Modifier.weight(1f))

        val disabledContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)

        Button(
            modifier = Modifier.height(36.dp),
            enabled = sendMessageEnabled,
            onClick = onMessageSent,
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = Color.Transparent,
                disabledContentColor = disabledContentColor
            ),
            border = border,
            contentPadding = PaddingValues(0.dp)
        ){
            Text(
                text = "Send",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }

}

@Composable
fun InputSelectorButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    selected: Boolean
){
    val backgroundModifier = if (selected){
        Modifier.background(
            color = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(14.dp)
        )
    } else{
        Modifier
    }

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp)
            .then(backgroundModifier)
    ) {
        val tint = if (selected){
            MaterialTheme.colorScheme.onSecondary
        } else {
            MaterialTheme.colorScheme.secondary
        }
        Icon(
            imageVector = icon,
            tint = tint,
            modifier = Modifier.padding(16.dp),
            contentDescription = description
        )
    }
}