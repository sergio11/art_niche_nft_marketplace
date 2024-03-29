package com.dreamsoftware.artcollectibles.ui.screens.mint

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory
import com.dreamsoftware.artcollectibles.domain.usecase.impl.CreateArtCollectibleUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetArtCollectibleCategoriesUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MintNftViewModel @Inject constructor(
    private val createArtCollectibleUseCase: CreateArtCollectibleUseCase,
    private val getArtCollectibleCategoriesUseCase: GetArtCollectibleCategoriesUseCase,
    private val applicationAware: IApplicationAware
) : SupportViewModel<MintNftUiState>() {

    companion object {
        const val MIN_NFT_NAME_LENGTH = 6
        const val MIN_NFT_DESCRIPTION_LENGTH = 10
    }

    override fun onGetDefaultState(): MintNftUiState = MintNftUiState()

    fun load() {
        onRequestingCameraPermission(true)
        fetchArtCollectibleCategories()
    }

    fun onImageSelected(imageUri: Uri, mimeType: String) {
        applicationAware.getFileProviderAuthority()
        updateState {
            it.copy(
                imageUri = imageUri,
                mimeType = mimeType
            )
        }
    }

    fun onResetImage() {
        updateState { it.copy(imageUri = null) }
    }

    fun onNameChanged(name: String) {
        updateState {
            it.copy(
                name = name,
                isCreateButtonEnabled = createButtonShouldBeEnabled(
                    name = name,
                    description = it.description.orEmpty()
                )
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        updateState {
            it.copy(
                description = description,
                isCreateButtonEnabled = createButtonShouldBeEnabled(name = it.name, description)
            )
        }
    }

    fun onRoyaltyChanged(newRoyalty: Float) {
        updateState {
            it.copy(
                royalty = newRoyalty
            )
        }
    }

    fun onCategoryChanged(category: ArtCollectibleCategory) {
        updateState {
            it.copy(categorySelected = category)
        }
    }

    fun onAddNewTag(newTag: String) {
        updateState {
            it.copy(
                tags = buildList {
                    addAll(it.tags)
                    add(newTag)
                }
            )
        }
    }

    fun onDeleteTag(tag: String) {
        updateState { state ->
            state.copy(tags = buildList {
                state.tags.filterNot { it == tag }.let(::addAll)
            })
        }
    }

    fun onRequestingCameraPermission(enabled: Boolean) {
        updateState {
            it.copy(isRequestingCameraPermission = enabled)
        }
    }

    fun onCameraPermissionStateChanged(granted: Boolean) {
        updateState {
            it.copy(
                isRequestingCameraPermission = false,
                isCameraPermissionGranted = granted
            )
        }
    }

    fun onCreate() {
        with(uiState.value) {
            onLoading()
            createArtCollectibleUseCase.invoke(
                scope = viewModelScope,
                params = CreateArtCollectibleUseCase.Params(
                    imagePath = imageUri.toString(),
                    mediaType = mimeType,
                    name = name,
                    description = description,
                    royalty = royalty.toLong(),
                    tags = tags,
                    categoryUid = categorySelected?.uid ?: categories.first().uid,
                    deviceName = applicationAware.getDeviceName()
                ),
                onSuccess = ::onCreateSuccess,
                onError = ::onCreateError
            )
        }
    }

    fun onConfirmCancelMintNftVisibilityChanged(isVisible: Boolean) {
        updateState { it.copy(isConfirmCancelMintNftVisible = isVisible) }
    }

    fun onHelpDialogVisibilityChanged(isVisible: Boolean) {
        updateState { it.copy(isHelpDialogVisible = isVisible) }
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onCreateSuccess(artCollectible: ArtCollectible) {
        updateState {
            it.copy(
                isLoading = false,
                isTokenMinted = true
            )
        }
    }

    private fun onCreateError(ex: Exception) {
        updateState { it.copy(isLoading = false) }
        ex.printStackTrace()
    }

    private fun onCategoriesLoaded(categories: Iterable<ArtCollectibleCategory>) {
        updateState {
            it.copy(
                categorySelected = categories.first(),
                categories = categories
            )
        }
    }

    private fun createButtonShouldBeEnabled(name: String, description: String) =
        description.length > MIN_NFT_DESCRIPTION_LENGTH
                && name.length > MIN_NFT_NAME_LENGTH

    private fun fetchArtCollectibleCategories() {
        getArtCollectibleCategoriesUseCase.invoke(
            scope = viewModelScope,
            onSuccess = ::onCategoriesLoaded,
            onError = {
                it.printStackTrace()
            }
        )
    }

}

data class MintNftUiState(
    val isLoading: Boolean = false,
    val imageUri: Uri? = null,
    val mimeType: String = "",
    val name: String = "",
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val royalty: Float = 0f,
    val isCameraPermissionGranted: Boolean = false,
    val isRequestingCameraPermission: Boolean = false,
    val categorySelected: ArtCollectibleCategory? = null,
    val isCreateButtonEnabled: Boolean = false,
    val isTokenMinted: Boolean = false,
    val categories: Iterable<ArtCollectibleCategory> = emptyList(),
    val isConfirmCancelMintNftVisible: Boolean = false,
    val isHelpDialogVisible: Boolean = false
)