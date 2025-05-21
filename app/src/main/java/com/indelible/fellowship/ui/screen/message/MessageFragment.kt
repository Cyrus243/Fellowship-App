package com.indelible.fellowship.ui.screen.message

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.indelible.fellowship.R
import com.indelible.fellowship.core.domain.ANIMATION_DURATION
import com.indelible.fellowship.core.domain.convertLongToTime
import com.indelible.fellowship.core.model.Conversation
import com.indelible.fellowship.core.model.MessageStatus
import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.core.model.UserStatus
import com.indelible.fellowship.navigation.Graph
import com.indelible.fellowship.navigation.MessageDetailScreens
import com.indelible.fellowship.ui.component.RoundProfile
import com.indelible.fellowship.ui.screen.message.viewmodel.MessageViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageFragment(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    navigate: (String) -> Unit,
    viewModel: MessageViewModel = hiltViewModel(),
){

    val emptyState = remember {
        mutableStateOf(false)
    }

    val uiState by viewModel.uiState.collectAsState()
    val conversations by viewModel.messageRowItems.collectAsState(emptyList())
    val users by viewModel.userList.collectAsState(emptyList())

    LaunchedEffect(key1 = Unit){
        delay(250)
        if (conversations.isEmpty()){
            emptyState.value = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 74.dp, top = 16.dp)
    ){

        Column {
            SearchBar(
                modifier = Modifier
                    //.requiredHeight(50.dp)
                    .align(CenterHorizontally),
                query = uiState.searchText,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = null)
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    }
                },
                placeholder = { Text(text = "Search your Chat")},
                onQueryChange = { viewModel.updateSearchText(it) },
                onSearch = {
                    viewModel.updateActiveSearch(false)
                    viewModel.updateSearchText("")
                },
                active = uiState.active,
                onActiveChange = {viewModel.updateActiveSearch(it)}
            ) {}


//            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
//                CustomTextField(
//                    text = remember { mutableStateOf("") },
//                    textStyle = MaterialTheme.typography.bodyMedium,
//                    placeholderText = "search your Chat",
//                    modifier = Modifier
//                        .padding(horizontal = 8.dp)
//                        .requiredHeight(46.dp),
//                    leadingIcon = {
//                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
//                    },
//                     trailingIcon = {
//                         Row {
//                             Row {
//                        IconButton(onClick = { /*TODO*/ }) {
//                            Icon(imageVector = Icons.Default.Mic, contentDescription = null)
//                        }
//                        IconButton(onClick = { /*TODO*/ }) {
//                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
//                        }
//                    }
//                         }
//                     }
//                )
//
//            }

            Spacer(modifier = Modifier.height(16.dp))

//            Text(
//                modifier = Modifier.padding(start = 16.dp),
//                text = "Messages",
//                style = MaterialTheme.typography.titleSmall
//            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                if (emptyState.value){
                    MessageEmptyState(modifier = Modifier.align(Alignment.TopCenter))
                }else {
                    LazyColumn(
                        state = lazyListState
                    ){
                        itemsIndexed(
                            conversations.sortedByDescending { it.lastMessage.timestamps },
                        ){ index, item ->
                            val user = viewModel.getUser(users, item.opponentID)
                            if (user != null){
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    ActionRow(
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                    MessageItem(
                                        item = item,
                                        opponent = user,
                                        onClick = navigate,
                                        chatRoomId = item.chatRoomId,
                                        onExpand = { viewModel.onItemExpanded(index)},
                                        onCollapsed = {viewModel.onItemCollapsed(index)},
                                        isRevealed = uiState.revealedItemIdsList.contains(index)
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 16.dp),
            shape = RoundedCornerShape(16.dp),
            onClick =  {navigate.invoke(Graph.START_CHAT)},
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Edit,
                    contentDescription = null)
//                Spacer(modifier = Modifier.width(4.dp))
//
//                AnimatedVisibility (lazyListState.isScrollingUp()){
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(text = stringResource(id = R.string.start_chat_button_text))
//                }
            }
        }
    }

}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun MessageItem(
    item: Conversation = Conversation(),
    opponent: User = User(),
    chatRoomId: String = "",
    onClick: (String) -> Unit = {},
    cardOffset: Float = -490f,
    onExpand: ()-> Unit,
    onCollapsed: ()-> Unit,
    isRevealed: Boolean
) {

    var offsetX by remember { mutableStateOf(0f) }
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")

    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) cardOffset - offsetX else -offsetX },

        )

    val cardElevation by transition.animateDp(
        label = "cardElevation",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) 8.dp else 0.dp }
    )

    val nameTextStyle = if (item.lastMessage.messageStatus == MessageStatus.READ)
        MaterialTheme.typography.bodyLarge
    else MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)

    val msgAndTimeTextStyle = if (item.lastMessage.messageStatus == MessageStatus.READ)
        MaterialTheme.typography.bodyMedium
    else MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)

    Surface(
        modifier = Modifier
            .offset { IntOffset((offsetX + offsetTransition).roundToInt(), 0) }
            .pointerInput(key1 = Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    val original = Offset(offsetX, 0f)
                    val summed = original + Offset(x = dragAmount, y = 0f)
                    val newValue = Offset(x = summed.x.coerceIn(cardOffset, 0f), y = 0f)

                    if (newValue.x <= -10) {
                        onExpand()
                        return@detectHorizontalDragGestures
                    } else if (newValue.x >= 0) {
                        onCollapsed()
                        return@detectHorizontalDragGestures
                    }
                    if (change.positionChange() != Offset.Zero) change.consume()
                    offsetX = newValue.x
                }
            }
            //.clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            .clickable {
                onClick.invoke(
                    MessageDetailScreens
                        .MessageDetailScreen
                        .route + "/$chatRoomId" + "/${opponent.userId}"
                )
            },
        tonalElevation = cardElevation
    ) {

        ListItem(
            headlineContent = {
                Text(
                    text = opponent.name,
                    style = nameTextStyle,
                )
            },
            supportingContent = {
                Text(
                    text = item.lastMessage.content,
                    style = msgAndTimeTextStyle,
                    modifier = Modifier.requiredWidth(230.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            },
            leadingContent = {
                RoundProfile(
                    modifier = Modifier.requiredSize(50.dp),
                    imagePath = opponent.photoPath,
                    isOnline = opponent.status == UserStatus.ONLINE
                )
            },
            trailingContent = {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text = convertLongToTime(item.lastMessage.timestamps),
                        style = msgAndTimeTextStyle,
                        color = Color.Gray
                    )

                    if (item.lastMessage.messageStatus == MessageStatus.RECEIVED){
                        Surface(
                            modifier = Modifier.requiredSize(10.dp),
                            shape = CircleShape,
                            color = Color(0xFF56638A),

                            ){}
                    }

                }
            }
        )
    }
}


@Composable
fun MessageEmptyState(modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.requiredSize(325.dp),
            painter = painterResource(id = R.drawable.message_concept_empty_state),
            contentDescription = null
        )

        Text(
            modifier = Modifier.requiredWidth(250.dp),
            text = "Your inbox is empty.",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.requiredWidth(250.dp),
            text = "Once you start a new conversation, you'll see it listed here",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionRow(modifier: Modifier = Modifier){
    Row(modifier = modifier) {
        Button(
            modifier = Modifier
                .size(70.dp),
            shape = RectangleShape,
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsOff,
                contentDescription = "Disable notifications"
            )
        }


        Button(
            modifier = Modifier
                .size(70.dp),
            shape = RectangleShape,
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                imageVector = Icons.Default.Archive,
                contentDescription = "Put message in archive folder"
            )
        }
    }
}


private const val TAG = "MessageFragment"