package com.dreamsoftware.artcollectibles.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.AccountBalance
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.domain.usecase.impl.CloseSessionUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetCurrentBalanceUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.GetAuthUserProfileUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.UpdateUserInfoUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import com.dreamsoftware.artcollectibles.utils.IApplicationAware
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Profile View Model
 * @param applicationAware
 * @param getAuthUserProfileUseCase
 * @param getCurrentBalanceUseCase
 * @param updateUserInfoUseCase
 * @param closeSessionUseCase
 * @param profileScreenErrorMapper
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val applicationAware: IApplicationAware,
    private val getAuthUserProfileUseCase: GetAuthUserProfileUseCase,
    private val getCurrentBalanceUseCase: GetCurrentBalanceUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val closeSessionUseCase: CloseSessionUseCase,
    private val profileScreenErrorMapper: ProfileScreenErrorMapper
) : SupportViewModel<ProfileUiState>() {

    override fun onGetDefaultState(): ProfileUiState = ProfileUiState()

    fun isProfileLoaded() = uiState.value.userInfo != null

    fun onInfoChanged(newInfo: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(info = newInfo))
            }
        }
    }

    fun onPictureChanged(imageUri: Uri) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(
                    userInfo = it.userInfo?.copy(photoUrl = imageUri.toString()),
                    isProfilePictureUpdated = true
                )
            }
        }
    }

    fun onBirthdateChanged(newBirthdate: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(birthdate = newBirthdate))
            }
        }
    }

    fun onNameChanged(newName: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(name = newName))
            }
        }
    }

    fun onProfessionalTitleChanged(newProfessionalTitle: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(professionalTitle = newProfessionalTitle))
            }
        }
    }

    fun onLocationChanged(newLocation: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(location = newLocation))
            }
        }
    }

    fun onCountryChanged(newCountry: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(country = newCountry))
            }
        }
    }

    fun onInstagramNickChanged(newInstagramNick: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.copy(instagramNick = newInstagramNick))
            }
        }
    }

    fun onAddNewTag(newTag: String) {
        if (isProfileLoaded()) {
            updateState {
                it.copy(userInfo = it.userInfo?.let { userInfo ->
                    userInfo.copy(tags = buildList {
                        userInfo.tags?.let(::addAll)
                        add(newTag)
                    })
                })
            }
        }
    }

    fun onDeleteTag(tag: String) {
        if (isProfileLoaded()) {
            updateState { state ->
                state.copy(userInfo = state.userInfo?.let { userInfo ->
                    userInfo.copy(tags = buildList {
                        userInfo.tags?.filterNot { it == tag }?.let(::addAll)
                    })
                })
            }
        }
    }

    fun load() {
        onLoading()
        loadProfileData()
    }

    fun getFileProviderAuthority() = applicationAware.getFileProviderAuthority()

    fun saveUserInfo() {
        with(uiState.value) {
            userInfo?.let {
                onLoading()
                updateUserInfoUseCase.invoke(
                    scope = viewModelScope,
                    params = UpdateUserInfoUseCase.Params(
                        userInfo = it,
                        isProfilePictureUpdated = isProfilePictureUpdated
                    ),
                    onSuccess = ::onProfileUpdated,
                    onError = ::onErrorOccurred
                )
            }
        }
    }

    fun closeSession() {
        onLoading()
        closeSessionUseCase.invoke(
            scope = viewModelScope,
            onSuccess = {
                onSessionClosed()
            }, onError = ::onErrorOccurred
        )
    }

    fun onCloseSessionDialogVisibilityChanged(isVisible: Boolean) {
        updateState { it.copy(isCloseSessionDialogVisible = isVisible) }
    }

    /**
     * Private Methods
     */

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                val fetchAuthUserDeferred = async { fetchAuthUser() }
                val fetchCurrentBalanceDeferred = async { fetchCurrentBalance() }
                val userProfile = fetchAuthUserDeferred.await()
                val currentBalance = fetchCurrentBalanceDeferred.await()
                onLoadProfileDataCompleted(userProfile, currentBalance)
            } catch (ex: Exception) {
                ex.printStackTrace()
                onErrorOccurred(ex)
            }
        }
    }

    private fun onLoading() {
        updateState {
            it.copy(
                isLoading = true,
                errorMessage = null
            )
        }
    }

    private fun onLoadProfileDataCompleted(userInfo: UserInfo, currentBalance: AccountBalance) {
        updateState {
            it.copy(
                userInfo = userInfo,
                isLoading = false,
                isProfilePictureUpdated = false,
                accountBalance = currentBalance
            )
        }
    }

    private fun onProfileUpdated(userInfo: UserInfo) {
        updateState {
            it.copy(
                userInfo = userInfo,
                isLoading = false,
                isProfilePictureUpdated = false,
            )
        }
    }

    private fun onErrorOccurred(ex: Exception) {
        updateState {
            it.copy(
                isLoading = false,
                errorMessage = profileScreenErrorMapper.mapToMessage(ex)
            )
        }
    }

    private fun onSessionClosed() {
        updateState {
            it.copy(
                isLoading = false,
                isSessionClosed = true,
                errorMessage = null
            )
        }
    }

    private suspend fun fetchAuthUser() =
        getAuthUserProfileUseCase.invoke(scope = viewModelScope)

    private suspend fun fetchCurrentBalance() =
        getCurrentBalanceUseCase.invoke(scope = viewModelScope, params = GetCurrentBalanceUseCase.Params())
}

data class ProfileUiState(
    val userInfo: UserInfo? = null,
    val accountBalance: AccountBalance? = null,
    val isProfilePictureUpdated: Boolean = false,
    val isLoading: Boolean = false,
    val isSessionClosed: Boolean = false,
    val isCloseSessionDialogVisible: Boolean = false,
    val errorMessage: String? = null
)