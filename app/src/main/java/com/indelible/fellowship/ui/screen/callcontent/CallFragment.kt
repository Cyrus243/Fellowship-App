package com.example.fellowship.ui.callcontent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.indelible.fellowship.R
import com.indelible.fellowship.core.domain.isScrollingUp
import com.indelible.fellowship.ui.screen.callcontent.CallItem


@Preview(showBackground = true)
@Composable
fun CallPreview(){
    val state = rememberLazyListState()
    CallFragment(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallFragment(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
){

    val dummyData = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

    Box(modifier = modifier
        .fillMaxSize()
        .padding(bottom = 74.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                query = "",
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                },
                placeholder = { Text(text = "Search your Chat")},
                onQueryChange = {},
                onSearch = {},
                active = false,
                onActiveChange = {}
            ) {

            }
            if (true){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Image(
                        modifier = Modifier.requiredSize(325.dp),
                        painter = painterResource(id = R.drawable.calls_empty_state),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.requiredWidth(250.dp),
                        text = "No Phone Call",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier.requiredWidth(250.dp),
                        text = "You didn't made any conversation yet, please select username.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }else{
                LazyColumn(){
                    items(dummyData){
                        CallItem()
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            onClick =  {},
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Call,
                    contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))

                AnimatedVisibility (state.isScrollingUp()){
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Make call")
                }
            }
        }

    }
}
