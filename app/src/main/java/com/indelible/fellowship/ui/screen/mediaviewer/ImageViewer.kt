package com.indelible.fellowship.ui.screen.mediaviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.indelible.fellowship.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewer(
    imageLink: String ,
    navigateUp:()-> Unit
){

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Color.Black, darkIcons = false)
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            navigationIcon = {
                IconButton(onClick = { navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            title = {}
        )
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(imageLink)
                .crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.baseline_account_circle_24),
            contentDescription = null
        )
    }
}