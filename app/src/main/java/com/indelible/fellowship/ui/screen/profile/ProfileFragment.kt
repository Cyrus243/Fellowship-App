package com.indelible.fellowship.ui.screen.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.indelible.fellowship.R
import com.indelible.fellowship.core.model.User


@Composable
fun ProfileFragment(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    openAndPopUp: (Any, Any) -> Unit,
    navigate: (String) -> Unit,
    popUp: () -> Unit
) {

    val user by viewModel.user.collectAsState(initial = User())
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ){ uri ->
        if (uri != null)
            viewModel.uploadProfileImage(uri)
    }
    
    val profileImage = if(user?.photoPath.isNullOrBlank()) R.drawable.baseline_account_circle_24
            else user?.photoPath
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            
            Box{
                AsyncImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .requiredSize(120.dp),
                    model = user?.photoPath,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )

                Surface(
                    shape = CircleShape,
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .requiredSize(45.dp)
                        .clip(CircleShape)
                        .align(Alignment.BottomEnd)
                        .clickable {
                            pickMedia.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                    ,
                ) {

                    Icon(imageVector = Icons.Default.Edit,
                        modifier = Modifier.padding(8.dp),
                        tint = Color.Gray,
                        contentDescription = null)
                }
            }
        }
        
        NameAnMail(user = user)

        Text(
            text = "About Me",
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        val statusText = if (user?.biography.isNullOrBlank())
            stringResource(id = R.string.empty_status_message)
        else user!!.biography
        
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = statusText,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))



        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatisticItem("Trends", "25")
                Divider(modifier = Modifier
                    .requiredHeight(36.dp)
                    .width(1.dp)
                )

                StatisticItem("Followers", "340")
                Divider(modifier = Modifier
                    .requiredHeight(36.dp)
                    .width(1.dp)
                )

                StatisticItem("Stars", "25")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.setting_title),
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsItem(
            title = stringResource(id = R.string.theme_button),
            icon = Icons.Default.DarkMode,
            trailing = { Switch(checked = false, onCheckedChange = {}) }
        ){}
        SettingsItem(
            title = stringResource(id = R.string.settings_button),
            icon = Icons.Default.Sms){}
        SettingsItem(
            title = stringResource(id = R.string.notification_button),
            icon = Icons.Default.Notifications){}
        SettingsItem(
            title = stringResource(id = R.string.logout_button),
            icon = Icons.AutoMirrored.Filled.Logout
        ){
            viewModel.onLogOutClick(openAndPopUp)
        }
    }
}

@Composable
fun ColumnScope.NameAnMail(
    user: User?
){
    
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = user?.name ?: "",
        modifier = Modifier.align(CenterHorizontally),
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        text = user?.email ?: "",
        modifier = Modifier.align(CenterHorizontally),
        style = MaterialTheme.typography.bodySmall
    )

    Spacer(modifier = Modifier.height(8.dp))
    
}

@Composable
fun StatisticItem(
    title: String,
    data: String
){
    Column(
        modifier = Modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = data,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

private const val TAG = "ProfileFragment"