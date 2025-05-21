package com.indelible.fellowship.ui.screen.message

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.indelible.fellowship.core.model.User
import com.indelible.fellowship.core.model.UserStatus

@SuppressLint("UnusedTransitionTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationAppBar(
    backPressHandler: () -> Unit,
    user: User,
    onDelete: ()-> Unit,
    onCopy: () -> Unit,
    onClose: () -> Unit,
    onReply: ()-> Unit,
    selectedItemList: List<Int>,
    scrollBehavior: TopAppBarScrollBehavior
){

    val transitionState = remember {
        MutableTransitionState(selectedItemList.isEmpty()).apply {
            targetState = selectedItemList.isNotEmpty()
        }
    }
    val transition = updateTransition(targetState = transitionState, "topBarTransition")

    val shadowElevation by transition.animateDp(
        label = "shadowElevation",
        transitionSpec = { tween(600) },
        targetValueByState = { if (selectedItemList.isNotEmpty()) 8.dp else 0.dp }
    )

    Surface(
        shadowElevation = shadowElevation
    ) {
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            navigationIcon = {
                IconButton(onClick = { backPressHandler() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "back press handler"
                    )
                }
            },
            title = { AppBarTitle(user = user) },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.VideoCall,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null)
                }
            },
            scrollBehavior = scrollBehavior
        )

        AnimatedVisibility(
            visible = selectedItemList.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TopAppBar(
                title = {
                    Text(
                        modifier  = Modifier.padding(start = 8.dp),
                        text = selectedItemList.size.toString(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onClose() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = MaterialTheme.colorScheme.secondary,
                            contentDescription = null)
                    }
                },
                actions = {
                    SelectedActionsTopBar(
                        onCopy = onCopy,
                        onDelete = onDelete,
                        isElementSelected = selectedItemList.size <= 1,
                        onReply = onReply
                    )
                  },
                scrollBehavior = scrollBehavior
            )
        }

    }
}

@Composable
fun SelectedActionsTopBar(
    onCopy: ()-> Unit,
    onReply: ()-> Unit,
    isElementSelected: Boolean,
    onDelete: () -> Unit
){

    if (isElementSelected){
        IconButton(onClick = onReply) {
            Icon(
                imageVector = Icons.Default.Reply,
                contentDescription = null
            )
        }

        IconButton(onClick = onCopy) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null
            )
        }
    }

    IconButton(onClick = onDelete) {
        Icon(
            imageVector = Icons.Default.Delete,
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
    }

    if (isElementSelected){
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }
    }

}


@Composable
fun AppBarTitle(user: User){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(42.dp)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                .clip(CircleShape),
            model = user.photoPath,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        NameAndStatus(
            name = user.name,
            status = user.status == UserStatus.ONLINE
        )
    }
}
