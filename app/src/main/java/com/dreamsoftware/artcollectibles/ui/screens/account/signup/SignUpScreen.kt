package com.dreamsoftware.artcollectibles.ui.screens.account.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.screens.account.core.AccountScreen
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit,
    onGoToSignIn: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by produceUiState(
        initialState = SignUpUiState(),
        lifecycle = lifecycle,
        viewModel = viewModel
    )
    val snackBarHostState = remember { SnackbarHostState() }
    with(viewModel) {
        SignUpComponent(
            uiState = state,
            snackBarHostState = snackBarHostState,
            onEmailChanged = ::onEmailChanged,
            onPasswordChanged = ::onPasswordChanged,
            onSignUp = ::signUp,
            onSignUpSuccess = onSignUpSuccess,
            onGoToSignIn = onGoToSignIn
        )
    }
}

@Composable
private fun SignUpComponent(
    uiState: SignUpUiState,
    snackBarHostState: SnackbarHostState,
    onEmailChanged: (email: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onSignUp: () -> Unit,
    onSignUpSuccess: () -> Unit,
    onGoToSignIn: () -> Unit
) {
    with(uiState) {
        if (state is SignUpState.OnError) {
            val signUpFailedText = stringResource(id = R.string.signup_signup_failed)
            LaunchedEffect(snackBarHostState) {
                snackBarHostState.showSnackbar(
                    message = signUpFailedText
                )
            }
        }
        CommonDialog(
            isVisible = state is SignUpState.OnSuccess,
            titleRes = R.string.signup_success_dialog_title_text,
            descriptionRes = R.string.signup_success_dialog_description_text,
            acceptRes = R.string.signup_success_dialog_accept_button_text,
            onAcceptClicked = onSignUpSuccess
        )
        LoadingDialog(isShowingDialog = state is SignUpState.InProgress)
        AccountScreen(
            snackBarHostState = snackBarHostState,
            mainTitleRes = R.string.signup_main_title_text,
            screenBackgroundRes = R.drawable.common_background
        ) {
            CommonText(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                type = CommonTextTypeEnum.TITLE_LARGE,
                titleRes = R.string.onboarding_subtitle_text,
                textColor = Purple500,
                textAlign = TextAlign.Center,
                textBold = true
            )
            CommonText(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                type = CommonTextTypeEnum.TITLE_MEDIUM,
                titleRes = R.string.signup_secondary_title_text,
                textColor = Purple700,
                textAlign = TextAlign.Center
            )
            CommonDefaultTextField(
                labelRes = R.string.signup_input_email_label,
                placeHolderRes = R.string.signup_input_email_placeholder,
                keyboardType = KeyboardType.Email,
                value = email,
                onValueChanged = onEmailChanged
            )
            CommonTextFieldPassword(
                labelRes = R.string.signup_input_password_label,
                placeHolderRes = R.string.signup_input_password_placeholder,
                value = password,
                onValueChanged = onPasswordChanged
            )
            Spacer(modifier = Modifier.padding(bottom = 20.dp))
            CommonButton(
                enabled = isSignUpButtonEnabled,
                text = R.string.signup_signup_button_text,
                onClick = onSignUp
            )
            CommonButton(
                containerColor = Purple40,
                text = R.string.signup_already_has_account_text,
                onClick = onGoToSignIn
            )
        }
    }
}