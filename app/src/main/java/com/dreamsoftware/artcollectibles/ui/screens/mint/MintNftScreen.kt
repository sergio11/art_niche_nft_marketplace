package com.dreamsoftware.artcollectibles.ui.screens.mint

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.extensions.checkPermissionState
import com.dreamsoftware.artcollectibles.ui.extensions.getCacheSubDir
import com.dreamsoftware.artcollectibles.ui.extensions.getMimeType
import com.dreamsoftware.artcollectibles.ui.theme.DarkPurple
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private val ROYALTY_RANGE = 0.0f..40.0f
private const val ROYALTY_STEPS = 3

@Composable
fun MintNftScreen(
    viewModel: MintNftViewModel = hiltViewModel(),
    onNftCreated: () -> Unit,
    onBackCalled: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle
    val state by produceUiState(
        initialState = MintNftUiState(),
        lifecycle = lifecycle,
        viewModel = viewModel
    )
    val context = LocalContext.current
    val outputDirectory = context.getCacheSubDir("nft_images")
    val cameraExecutor = Executors.newSingleThreadExecutor()
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load()
        }
        if(state.isRequestingCameraPermission) {
            CheckCameraPermission(
                context = context,
                onCameraPermissionStateChanged = ::onCameraPermissionStateChanged
            )
        }
        MintNftComponent(
            state = state,
            lifecycleOwner = lifecycleOwner,
            context = context,
            outputDirectory = outputDirectory,
            executor = cameraExecutor,
            onImageCaptured = {
                with(context) {
                    onImageSelected(
                        imageUri = it,
                        mimeType = getMimeType(it).orEmpty()
                    )
                }
            },
            onRequestingCameraPermission = ::onRequestingCameraPermission,
            onNameChanged = ::onNameChanged,
            onDescriptionChanged = ::onDescriptionChanged,
            onRoyaltyChanged = ::onRoyaltyChanged,
            onCreateClicked = ::onCreate,
            onNftCreatedSuccess = onNftCreated,
            onExitClicked = onBackCalled,
            onAddNewTag = ::onAddNewTag,
            onDeleteTag = ::onDeleteTag,
            onCategoryChanged = ::onCategoryChanged,
            onResetImage = ::onResetImage,
            onConfirmCancelMintNftVisibilityChanged = ::onConfirmCancelMintNftVisibilityChanged,
            onHelpDialogVisibilityChanged = ::onHelpDialogVisibilityChanged
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MintNftComponent(
    state: MintNftUiState,
    lifecycleOwner: LifecycleOwner,
    context: Context,
    outputDirectory: File,
    executor: Executor,
    onRequestingCameraPermission: (Boolean) -> Unit,
    onImageCaptured: (Uri) -> Unit,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onRoyaltyChanged: (Float) -> Unit,
    onCreateClicked: () -> Unit,
    onNftCreatedSuccess: () -> Unit,
    onExitClicked: () -> Unit,
    onAddNewTag: (tag: String) -> Unit,
    onDeleteTag: (tag: String) -> Unit,
    onCategoryChanged: (ArtCollectibleCategory) -> Unit,
    onResetImage: () -> Unit,
    onConfirmCancelMintNftVisibilityChanged: (isVisible: Boolean) -> Unit,
    onHelpDialogVisibilityChanged: (isVisible: Boolean) -> Unit
) {
    with(state) {
        BackHandler(enabled = true) {
            onConfirmCancelMintNftVisibilityChanged(true)
        }
        CommonDialog(
            isVisible = isConfirmCancelMintNftVisible,
            titleRes = R.string.add_nft_cancel_confirm_title_text,
            descriptionRes = R.string.add_nft_cancel_confirm_description_text,
            acceptRes = R.string.add_nft_cancel_confirm_accept_button_text,
            cancelRes = R.string.add_nft_cancel_cancel_button_text,
            onAcceptClicked = {
                onExitClicked()
                onConfirmCancelMintNftVisibilityChanged(false)
            },
            onCancelClicked = { onConfirmCancelMintNftVisibilityChanged(false) }
        )
        CommonDialog(
            isVisible = isHelpDialogVisible,
            titleRes = R.string.add_nft_help_info_title_text,
            descriptionRes = R.string.add_nft_help_info_description_text,
            acceptRes = R.string.add_nft_help_info_accept_button_text,
            onAcceptClicked = {
                onHelpDialogVisibilityChanged(false)
            }
        )
        Scaffold(
            topBar = {
                CommonTopAppBar(
                    titleRes = R.string.add_nft_main_title_text,
                    centerTitle = true,
                    navigationAction = TopBarAction(
                        iconRes = R.drawable.back_icon,
                        onActionClicked = {
                            onConfirmCancelMintNftVisibilityChanged(true)
                        }
                    ),
                    menuActions = listOf(
                        TopBarAction(
                            iconRes = R.drawable.help_icon,
                            onActionClicked = {
                                onHelpDialogVisibilityChanged(true)
                            }
                        )
                    )
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (isCameraPermissionGranted) {
                    if (imageUri == null) {
                        CameraDisplayComponent(
                            lifecycleOwner = lifecycleOwner,
                            context = context,
                            outputDirectory = outputDirectory,
                            executor = executor,
                            onImageCaptured = onImageCaptured,
                            onError = {
                                Log.d("ART_COLLE", "ImageCaptureException -> ${it.message} CALLED!")
                            }
                        )
                    } else {
                        MintNftForm(
                            state = state,
                            onNameChanged = onNameChanged,
                            onDescriptionChanged = onDescriptionChanged,
                            onRoyaltyChanged = onRoyaltyChanged,
                            onCreateClicked = onCreateClicked,
                            onNftCreatedSuccess = onNftCreatedSuccess,
                            onAddNewTag = onAddNewTag,
                            onDeleteTag = onDeleteTag,
                            onCategoryChanged = onCategoryChanged,
                            onResetImage = onResetImage,
                            onConfirmCancelMintNftVisibilityChanged = onConfirmCancelMintNftVisibilityChanged
                        )
                    }
                } else {
                    ScreenBackgroundImage(imageRes = R.drawable.screen_background_2)
                    ErrorStateNotificationComponent(
                        isVisible = true,
                        imageRes = R.drawable.error_occurred,
                        title = stringResource(id = R.string.add_nft_no_camera_permission_granted_text),
                        isRetryButtonVisible = true,
                        onRetryCalled = {
                            onRequestingCameraPermission(true)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckCameraPermission(
    context: Context,
    onCameraPermissionStateChanged: (granted: Boolean) -> Unit
) {
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isSuccess ->
            onCameraPermissionStateChanged(isSuccess)
        }
    // run on every composition
    SideEffect {
        context.checkPermissionState(
            permission = Manifest.permission.CAMERA,
            onPermissionGranted = {
                onCameraPermissionStateChanged(true)
            },
            onPermissionDenied = {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        )
    }
}

@Composable
private fun MintNftForm(
    state: MintNftUiState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onRoyaltyChanged: (Float) -> Unit,
    onCreateClicked: () -> Unit,
    onNftCreatedSuccess: () -> Unit,
    onResetImage: () -> Unit,
    onAddNewTag: (tag: String) -> Unit,
    onDeleteTag: (tag: String) -> Unit,
    onCategoryChanged: (ArtCollectibleCategory) -> Unit,
    onConfirmCancelMintNftVisibilityChanged: (isVisible: Boolean) -> Unit
) {
    with(state) {
        LoadingDialog(isShowingDialog = isLoading)
        CommonDialog(
            isVisible = isTokenMinted,
            titleRes = R.string.add_nft_token_minted_confirm_title_text,
            descriptionRes = R.string.add_nft_token_minted_confirm_description_text,
            acceptRes = R.string.add_nft_token_minted_confirm_accept_button_text,
            onAcceptClicked = onNftCreatedSuccess
        )
        Box {
            ScreenBackgroundImage(imageRes = R.drawable.screen_background_2)
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                CommonCardColumn {
                    val defaultModifier = Modifier
                        .padding(vertical = 15.dp)
                        .width(300.dp)
                    Box {
                        CommonAsyncImage(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White, CircleShape),
                            context = LocalContext.current,
                            imageUri = imageUri
                        )
                        Image(
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .size(40.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .align(Alignment.BottomEnd)
                                .clickable { onResetImage() },
                            painter = painterResource(id = R.drawable.remove_nft_photo),
                            contentDescription = "Remove picture"
                        )
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    CommonText(
                        modifier = defaultModifier,
                        type = CommonTextTypeEnum.TITLE_MEDIUM,
                        titleRes = R.string.add_nft_subtitle_text,
                        textColor = DarkPurple,
                        textAlign = TextAlign.Center
                    )
                    CommonDefaultTextField(
                        modifier = defaultModifier,
                        labelRes = R.string.add_nft_input_name_label,
                        placeHolderRes = R.string.add_nft_input_name_placeholder,
                        value = name,
                        onValueChanged = onNameChanged
                    )
                    CategorySelectorInput(
                        modifier = defaultModifier,
                        category = categorySelected,
                        categories = categories,
                        labelRes = R.string.add_nft_input_category_label,
                        placeHolderRes = R.string.add_nft_input_category_placeholder,
                        onCategorySelected = onCategoryChanged
                    )
                    CommonSliderComponent(
                        modifier = defaultModifier,
                        title = "${stringResource(R.string.add_nft_input_royalty_label)} ${royalty.toLong()}%",
                        value = royalty,
                        valueRange = ROYALTY_RANGE,
                        steps = ROYALTY_STEPS,
                        onValueChange = onRoyaltyChanged
                    )
                    CommonDefaultTextField(
                        modifier = defaultModifier.height(150.dp),
                        labelRes = R.string.add_nft_input_description_label,
                        placeHolderRes = R.string.add_nft_input_description_placeholder,
                        value = description,
                        isSingleLine = false,
                        onValueChanged = onDescriptionChanged
                    )
                    CommonTagsInputComponent(
                        modifier = defaultModifier,
                        titleRes = R.string.add_nft_input_related_topic_label,
                        placeholderRes = R.string.add_nft_input_related_topic_placeholder,
                        tagList = tags,
                        onAddNewTag = onAddNewTag,
                        onDeleteTag = onDeleteTag
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    CommonButton(
                        enabled = !isLoading && isCreateButtonEnabled,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .width(300.dp),
                        text = R.string.add_nft_create_button_text,
                        containerColor = Purple700,
                        contentColor = Color.White,
                        onClick = onCreateClicked
                    )
                    CommonButton(
                        enabled = !isLoading,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .width(300.dp),
                        text = R.string.add_nft_cancel_button_text,
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        onClick = {
                            onConfirmCancelMintNftVisibilityChanged(true)
                        }
                    )
                }
            }
        }
    }
}