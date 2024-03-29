package com.dreamsoftware.artcollectibles.ui.screens.account.signin

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ExternalProviderAuthTypeEnum
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.screens.account.core.AccountScreen
import com.dreamsoftware.artcollectibles.ui.theme.ArtCollectibleMarketplaceTheme
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import com.dreamsoftware.artcollectibles.ui.theme.montserratFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onSignInSuccess: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceState(
        initialValue = SignInUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                if (it.loginState is LoginState.OnLoginSuccess) {
                    onSignInSuccess()
                } else {
                    value = it
                }
            }
        }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    with(viewModel) {
        SignInComponent(
            state = state,
            snackBarHostState = snackBarHostState,
            coroutineScope = coroutineScope,
            onEmailChanged = ::onEmailChanged,
            onPasswordChanged = ::onPasswordChanged,
            onSignIn = ::signIn,
            onSocialSignIn = ::signIn
        )
    }
}

@Composable
private fun SignInComponent(
    state: SignInUiState,
    snackBarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onSignIn: () -> Unit,
    onSocialSignIn: (token: String, authType: ExternalProviderAuthTypeEnum) -> Unit
) {
    with(state) {
        if (loginState is LoginState.OnLoginError) {
            val loginFailedText = stringResource(id = R.string.signin_login_failed)
            LaunchedEffect(snackBarHostState) {
                snackBarHostState.showSnackbar(
                    message = loginFailedText
                )
            }
        }

        LoadingDialog(isShowingDialog = loginState is LoginState.OnLoginInProgress)
        AccountScreen(
            snackBarHostState = snackBarHostState,
            mainTitleRes = R.string.signin_main_title_text,
            screenBackgroundRes = R.drawable.common_background) {
            CommonText(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
                type = CommonTextTypeEnum.TITLE_LARGE,
                titleRes = R.string.onboarding_subtitle_text,
                textColor = Purple500,
                textAlign = TextAlign.Center,
                textBold = true
            )
            CommonText(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
                type = CommonTextTypeEnum.TITLE_MEDIUM,
                titleRes = R.string.signin_secondary_title_text,
                textColor = Purple700,
                textAlign = TextAlign.Center
            )
            CommonDefaultTextField(
                labelRes = R.string.signin_input_email_label,
                placeHolderRes = R.string.signin_input_email_placeholder,
                keyboardType = KeyboardType.Email,
                value = email,
                onValueChanged = onEmailChanged
            )
            CommonTextFieldPassword(
                labelRes = R.string.signin_input_password_label,
                placeHolderRes = R.string.signin_input_password_placeholder,
                value = password,
                onValueChanged = onPasswordChanged
            )
            Spacer(modifier = Modifier.padding(bottom = 20.dp))
            CommonButton(
                enabled = isLoginButtonEnabled,
                text = R.string.signin_login_button_text,
                onClick = onSignIn
            )
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            AlternativeLoginDivider()
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            val loginFBCancelledText =
                stringResource(id = R.string.signin_login_facebook_cancelled)
            val loginFBFailedText =
                stringResource(id = R.string.signin_login_facebook_failed)
            FacebookLoginButton(
                enabled = loginState !is LoginState.OnLoginInProgress,
                modifier = Modifier.padding(horizontal = 40.dp),
                onSuccess = {
                    onSocialSignIn(it, ExternalProviderAuthTypeEnum.FACEBOOK)
                },
                onCancel = {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = loginFBCancelledText
                        )
                    }
                },
                onError = {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = loginFBFailedText
                        )
                    }
                }
            )
            val loginGGFailedText =
                stringResource(id = R.string.signin_login_google_failed)
            GoogleLoginButton(
                enabled = loginState !is LoginState.OnLoginInProgress,
                modifier = Modifier.padding(horizontal = 40.dp),
                onAuthFailed = {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = loginGGFailedText
                        )
                    }
                },
                onAuthSuccess = {
                    onSocialSignIn(it, ExternalProviderAuthTypeEnum.GOOGLE)
                }
            )
        }
    }
}

@Composable
internal fun AlternativeLoginDivider() {
    Row(
        modifier = Modifier.padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color.Gray
        )
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.signin_alternative_login_text),
            fontFamily = montserratFontFamily,
            color = Color.Gray
        )
        Divider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}


@Composable
@Preview
fun PreviewOnBoardingScreen() {
    ArtCollectibleMarketplaceTheme {
        SignInScreen {
            //Navigate to the next screen
        }
    }
}