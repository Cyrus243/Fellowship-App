package com.indelible.fellowship.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun RoundProfile(
    modifier: Modifier = Modifier,
    imagePath: String,
    isOnline: Boolean
){

    Box(modifier = modifier) {

//        AsyncImage(
//            modifier = Modifier
//                .clip(CircleShape),
//            model = imageLoader,
//            error = painterResource(id = R.drawable.baseline_account_circle_24),
//            contentScale = ContentScale.Crop,
//            contentDescription = null
//        )
        AsyncImage(
            model = imagePath,
            contentDescription = null,
            modifier = Modifier.clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        AnimatedVisibility (
            visible = isOnline,
            modifier = Modifier.align(Alignment.BottomEnd)
        ){
            Surface(
                shape = CircleShape,
                modifier = Modifier
                    .requiredSize(15.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.padding(2.dp),
                    color = Color.Green
                ) {}
            }
        }

    }
}