package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IPreferenceRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IUserRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCase
import com.dreamsoftware.artcollectibles.utils.IApplicationAware

/**
 * Close session use case
 * @param userRepository
 * @param preferenceRepository
 * @param applicationAware
 */
class CloseSessionUseCase(
    private val userRepository: IUserRepository,
    private val preferenceRepository: IPreferenceRepository,
    private val applicationAware: IApplicationAware
): BaseUseCase<Unit>() {
    override suspend fun onExecuted() {
        applicationAware.setUserSecretKey(key = null)
        userRepository.closeSession()
        preferenceRepository.clearData()
    }
}