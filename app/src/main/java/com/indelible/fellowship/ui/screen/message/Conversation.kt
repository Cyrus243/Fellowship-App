package com.indelible.fellowship.ui.screen.message

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.indelible.fellowship.core.domain.convertLongToDate
import com.indelible.fellowship.core.domain.convertLongToTime
import com.indelible.fellowship.core.domain.messageFormatter
import com.indelible.fellowship.core.model.Message
import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.navigation.Destination
import com.indelible.fellowship.ui.component.ConversationDropDown
import com.indelible.fellowship.ui.component.CustomAlertDialog
import com.indelible.fellowship.ui.component.UserInput
import com.indelible.fellowship.ui.screen.message.viewmodel.ConversationViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    navigate: (Any) -> Unit,
    popUp: () -> Unit,
    opponentId: String,
    chatRoomId: String,
    viewModel: ConversationViewModel = hiltViewModel()
){

    val messages by viewModel
        .getConversation(chatRoomId)
        .collectAsState(initial = emptyList())

    ConversationContent(
        modifier = modifier,
        navigate = navigate,
        popUp = popUp,
        opponentId = opponentId,
        chatRoomId = chatRoomId,
        viewModel = viewModel,
        messages = messages,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationContent(
    modifier: Modifier = Modifier,
    navigate: (Any) -> Unit,
    popUp: () -> Unit,
    opponentId: String,
    chatRoomId: String,
    messages: List<Message>,
    viewModel: ConversationViewModel,
){

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.White, darkIcons = true)

    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val sortedMessages = messages.sortedByDescending { it.timestamps }
    var expand by remember {
        mutableStateOf(false)
    }

    val scrollState = rememberLazyListState()
    val opponent by viewModel.getOpponent(opponentId).collectAsState(
            initial = User()
    )
    val user by viewModel.getOpponent(viewModel.currentUserId).collectAsState(
        initial = User()
    )

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                if (uiState.openDialog){
                    CustomAlertDialog (
                        onDismissRequest = { viewModel.updateOpenDialog(false) },
                        onConfirm = {
                            viewModel.deleteMessage(sortedMessages, chatRoomId)
                            viewModel.clearSelectedList()
                        }
                    )
                }
                Messages(
                    modifier = Modifier.weight(1f),
                    messages = sortedMessages,
                    scrollState = scrollState,
                    updateMessage = {
                        viewModel.changeMessageStatus(
                            message = it,
                            chatRoomId = chatRoomId
                        )
                    },
                    navigate = navigate,
                    popUp = popUp,
                    onMessageSelected = { viewModel.onItemSelected(it) },
                    onMessageUnSelected = { viewModel.onItemUnselected(it) },
                    itemSelectedList = uiState.selectedItemList,
                    scrollBehavior = scrollBehavior
                )
                UserInput(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .imePadding(),
                    onMessageSent = {
                        viewModel.postMessage(
                            opponentId = opponentId,
                            chatRoomId = chatRoomId,
                            userName = user?.name ?: "",
                            opponent = opponent ?: User(),
                            isMedia = false
                        )
                    },
                    onTextChanged = {viewModel.updateMessageField(it)},
                    uiState = uiState,
                    resetScroll = {
                            scope.launch {
                                scrollState
                                    .animateScrollToItem(0)
                            }
                    },
                    uploadMedia = { uri ->
                        viewModel.uploadMediaFile(
                            uri,
                            chatRoomId,
                            opponentId,
                            opponent = opponent ?: User(),
                            userName = user?.name ?: ""
                        )
                    },
                    expanded = expand,
                    onExpand = { expand = it },
                    message = uiState.replyMessage,
                    isMessageSelected = uiState.isMessageSelected,
                    onDismiss = { viewModel.updateMessageSelection(false, Message())},
                    onRecordPress = { viewModel.startTimer(it) }
                )
            }
            ConversationAppBar(
                backPressHandler = { popUp.invoke() },
                user = opponent ?: User(),
                onClose = { viewModel.clearSelectedList() },
                onCopy = {
                    viewModel.copyText(context,
                        sortedMessages[uiState.selectedItemList.first()].content
                    )
                    viewModel.clearSelectedList()
                 },
                onDelete = { viewModel.updateOpenDialog(true) },
                selectedItemList = uiState.selectedItemList,
                scrollBehavior = scrollBehavior,
                onReply = {
                    viewModel.updateMessageSelection(
                        selected = true,
                        message = sortedMessages[uiState.selectedItemList.first()]
                    )
                    viewModel.clearSelectedList()
                }
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Messages(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    scrollState: LazyListState,
    updateMessage: (Message) -> Unit,
    navigate: (Any) -> Unit,
    popUp: () -> Unit,
    onMessageSelected: (Int) -> Unit,
    onMessageUnSelected: (Int) -> Unit,
    itemSelectedList: List<Int>,
    scrollBehavior: TopAppBarScrollBehavior
){

    Box(modifier = modifier) {
        val authorMe = "me"

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            reverseLayout = true,
            state = scrollState,
            contentPadding = WindowInsets.statusBars
                .add(WindowInsets(top = 30.dp, left = 8.dp, right = 8.dp))
                .asPaddingValues()
        ){
            for (index in messages.indices){
                val prevAuthor = messages.getOrNull(index - 1)?.author
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val content = messages[index]
                val isFirstMessageByAuthor = prevAuthor != content.author
                val isLastMessageByAuthor = nextAuthor != content.author

                val nextTimeStamp = convertLongToDate(
                    messages.getOrNull(index + 1)?.timestamps
                )

                val encodeUrl = URLEncoder.encode(content.content, StandardCharsets.UTF_8.toString())
                item {
                    Message(
                        msg = content,
                        isUserMe = authorMe == content.author,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor,
                        openImage = { navigate(Destination.ImageViewer(encodeUrl)) },
                        onLongClick = {
                            if (itemSelectedList.contains(index)){
                                onMessageUnSelected(index)
                            } else {
                                onMessageSelected(index)
                            }
                        },
                        isSelected = itemSelectedList.contains(index),
                        isSelectedListEmpty = itemSelectedList.isEmpty()
                    )
                }

                if (nextTimeStamp.compareTo(convertLongToDate(content.timestamps)) != 0){
                    item {
                        DayHeader(dayString = convertLongToDate(content.timestamps))
                    }
                }
                updateMessage(content)

            }


        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Message(
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    openImage: () -> Unit,
    onLongClick: ()->Unit,
    isSelected: Boolean,
    isSelectedListEmpty: Boolean

){

    var expanded by remember {
        mutableStateOf(false)
    }

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    Row(
        modifier = spaceBetweenAuthors.fillMaxSize(),
        horizontalArrangement = if (isUserMe) Arrangement.End else Arrangement.Start
    ) {

        if (msg.media){
            ChatImageBubble(
                imageLink = msg.content,
                isUserMe = isUserMe,
                isFirstMessageByAuthor = isFirstMessageByAuthor,
                openImage = openImage
            )
        }else{
            val interactionSource = remember { MutableInteractionSource() }

            AuthorAndTextMessage(
                msg = msg,
                isUserMe = isUserMe,
                isFirstMessageByAuthor = isFirstMessageByAuthor,
                isLastMessageByAuthor = isLastMessageByAuthor,
                modifier = Modifier
                    .widthIn(max = 330.dp)
                    .combinedClickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onLongClick = { onLongClick() },
                        onClick = {
                            if (!isSelectedListEmpty) {
                                onLongClick()
                            }
                        }
                    ),
                expanded = expanded,
                onDismiss = { expanded  = false },
                isSelected = isSelected
            )
        }

    }
}

@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismiss: (Boolean)-> Unit,
    isSelected: Boolean
){

    Column(modifier = modifier) {
        ChatItemBubble(msg, isUserMe, modifier, isSelected)
        ConversationDropDown(
            expanded = expanded,
            onDismiss
        )
        if(isFirstMessageByAuthor){
            Spacer(modifier = Modifier.height(8.dp))
        }else {
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Composable
fun AuthorNameTimestamp(msg: Message) {
    Row() {
        Text(
            text = msg.author,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = convertLongToTime(msg.timestamps),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
private val UserChatBubbleShape = RoundedCornerShape(20.dp,4.dp,20.dp,20.dp)


@Composable
fun ChatItemBubble(
    message: Message,
    isUserMe: Boolean,
    modifier: Modifier = Modifier,
    isSelected: Boolean
){

    val opponentBackgroundBubbleColor = if (isSelected){
        MaterialTheme.colorScheme.primary
    }else{
        MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
    }

    val userBackgroundBubbleColor = if (isSelected){
        MaterialTheme.colorScheme.primary
    }else{
        MaterialTheme.colorScheme.primaryContainer
    }

    Surface(
        modifier = modifier,
        color = if (isUserMe) userBackgroundBubbleColor else opponentBackgroundBubbleColor,
        shape = if (isUserMe) UserChatBubbleShape else ChatBubbleShape
    ) {
        ClickableMessage(message, isUserMe)
    }


}


@Composable
fun ChatImageBubble(
    imageLink: String,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    openImage: ()-> Unit
){
    val backgroundBubbleColor = if (isUserMe){
        MaterialTheme.colorScheme.primary
    }else{
        MaterialTheme.colorScheme.secondary
    }

    val interactionSource = remember { MutableInteractionSource() }


    Column{
        val shape = if (isUserMe) UserChatBubbleShape else ChatBubbleShape
        Surface(
            modifier = Modifier
                .heightIn(max = 350.dp)
                .widthIn(max = 330.dp),
            color = backgroundBubbleColor,
            shape = shape
        ) {
            AsyncImage(
                model = imageLink,
                contentDescription = null,
                modifier = Modifier
                    .clip(shape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = openImage
                    ),
                contentScale = ContentScale.Fit
            )
        }
        if(isFirstMessageByAuthor){
            Spacer(modifier = Modifier.height(8.dp))
        }else {
            Spacer(modifier = Modifier.height(2.dp))
        }
    }

}

@Composable
fun ClickableMessage(
    message: Message,
    isUserMe: Boolean
){

    val styleMessage = messageFormatter(text = message.content,
        primary = isUserMe)
    Text(
        text = styleMessage,
        style = MaterialTheme.typography.bodyMedium.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp)
    )
}


@Composable
fun NameAndStatus(
    name: String,
    status: Boolean
){
    
    Column {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
        if (status){
            Text(
                text = "online",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }
    }
}

 const val TAGS = "ConversationFragment"