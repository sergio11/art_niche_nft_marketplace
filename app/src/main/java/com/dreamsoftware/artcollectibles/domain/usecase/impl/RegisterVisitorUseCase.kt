package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IVisitorsRepository
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams
import java.math.BigInteger

class RegisterVisitorUseCase(
    private val visitorsRepository: IVisitorsRepository
): BaseUseCaseWithParams<RegisterVisitorUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        with(params) {
            visitorsRepository.register(tokenId, userAddress)
        }
    }

    data class Params(
        val tokenId: BigInteger,
        val userAddress: String
    )
}