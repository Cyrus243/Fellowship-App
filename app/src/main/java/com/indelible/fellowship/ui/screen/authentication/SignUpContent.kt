package com.indelible.fellowship.ui.screen.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fellowship.ui.authentication.SignUpViewModel
import com.indelible.fellowship.R
import com.indelible.fellowship.SignUpUIState

@Composable
fun SignUpContent(
    openAndPopUp: (String, String) -> Unit,
    popUp: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {

    val uiState by signUpViewModel.uiState.collectAsState()

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                SignUPHeadTitle(Modifier.padding(horizontal = 16.dp))

                InformationTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onEmailChanged = { signUpViewModel.updateUserEmail(it)},
                    onPasswordChanged = { signUpViewModel.updatePassWord(it)},
                    onNameChanged = { signUpViewModel.updateUserName(it)},
                    uiState = uiState,
                    onVisibilityChanged = {signUpViewModel.onVisibilityClick()}
                )

                SignUpButton(modifier = Modifier,
                    onCreateClick = { signUpViewModel.onCreateAccount(openAndPopUp)},
                    onGoogleSignIn = {},
                )
            }

            TopBar(
                modifier = Modifier,
                popUp = { signUpViewModel.onPopUp(popUp) }
            )

        }
    }
}

@Composable
fun SignUpButton(
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit,
    onGoogleSignIn: () -> Unit,
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .requiredHeight(54.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = { onCreateClick.invoke() }
        ) {
            Text(text = stringResource(id = R.string.create_account_button))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .requiredHeight(54.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(color = Color.Transparent)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            onClick = { onGoogleSignIn() }
        ) {
            Image(painter = painterResource(id = R.drawable.google_icon),
                contentDescription = null)
            Spacer(modifier = modifier.width(8.dp))
            Text(text = stringResource(id = R.string.google_sign_up_button))
        }

    }
}

@Composable
fun InformationTextField(
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    uiState: SignUpUIState,
    onVisibilityChanged: () -> Unit

){

    val focusManager = LocalFocusManager.current
    val trailingIcon = if (uiState.isPasswordVisible) Icons.Filled.VisibilityOff
        else Icons.Filled.Visibility

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.name,
            onValueChange = { onNameChanged(it)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            label = { Text(
                text = stringResource(id = R.string.name_label),
                style = LocalTextStyle.current)}
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.email,
            onValueChange = {onEmailChanged(it)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            label = { Text(text = stringResource(id = R.string.email_label),
                style = LocalTextStyle.current)}
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.passWord,
            onValueChange = {onPasswordChanged(it)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            trailingIcon = {
                Icon(imageVector = trailingIcon,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onVisibilityChanged
                    ),
                    contentDescription = null)
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            label = { Text(text = stringResource(id = R.string.password_label),
                style = LocalTextStyle.current)}
        )
        

    }
}

@Composable
fun SignUPHeadTitle(
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = "Create an account",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier,
            text = "Let's build a community together!",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    popUp: () -> Unit
){
    TopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = {
            IconButton(onClick = popUp) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null)
            }
        }
    )
}

