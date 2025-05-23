package com.indelible.fellowship.ui.screen.authentication

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.indelible.fellowship.R
import com.indelible.fellowship.SignUpUIState


@Composable
fun LogInContent(
    viewModel: SignInViewModel = hiltViewModel(),
    openAndPopUp: (Any, Any) -> Unit,
    navigate: (Any) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit){
        viewModel.checkStatus(openAndPopUp)
    }

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                HeadTitle(Modifier.padding(horizontal = 16.dp))


                InformationTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onEmailChanged = { viewModel.updateUserEmail(it) },
                    onPasswordChanged = { viewModel.updatePassWord(it) },
                    onForgotPassword = {},
                    onDone = {focusManager.clearFocus()},
                    onVisibilityChanged = { viewModel.onVisibilityClick() },
                    uiState = uiState
                )

                LogInButton(modifier = Modifier,
                    onSignInClick = {
                        viewModel.onSignInClick(openAndPopUp)
                                    },
                    onGoogleSignIn = {},
                    isLogInEnable = viewModel.enableLogInButton()
                )

                SignUpButton {
                    viewModel.onSignUpClick(navigate)
                }
            }

        }
    }
}

@Composable
fun LogInButton(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    onGoogleSignIn: () -> Unit,
    isLogInEnable: Boolean
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
            enabled = isLogInEnable,
            shape = RoundedCornerShape(16.dp),
            onClick = { onSignInClick.invoke() }
        ) {
            Text(text = stringResource(id = R.string.sign_in_button))
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
            Text(
                text = stringResource(id = R.string.google_sign_in_button))
        }

    }
}

@Composable
fun InformationTextField(
    modifier: Modifier = Modifier,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onForgotPassword: () -> Unit,
    onDone: () -> Unit,
    onVisibilityChanged: () -> Unit,
    uiState: SignUpUIState
){

    val trailingIcon = if (uiState.isPasswordVisible) Icons.Filled.VisibilityOff else
        Icons.Filled.Visibility


    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.email,
            onValueChange = { onEmailChanged(it)},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = uiState.isNotValidEmail,
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
            isError = uiState.isNotValidPassWord,
            keyboardActions = KeyboardActions(onDone = { onDone.invoke() }),
            trailingIcon = {
                Icon(imageVector = trailingIcon,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onVisibilityChanged
                    ),
                    contentDescription = null)
            },
            visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.bodyMedium,
            label = { Text(text = stringResource(id = R.string.password_label),
                style = LocalTextStyle.current)}
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onForgotPassword
                )
                .align(Alignment.End),
            text = stringResource(id = R.string.forgot_password_text_button),
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
            textAlign = TextAlign.Center
        )

    }
}


@Composable
fun HeadTitle(modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.welcome_title),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.welcome_text),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SignUpButton(
    onClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(id = R.string.no_member_text),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
            text = stringResource(id = R.string.sign_up_text_button),
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
            textAlign = TextAlign.Center
        )
    }
}