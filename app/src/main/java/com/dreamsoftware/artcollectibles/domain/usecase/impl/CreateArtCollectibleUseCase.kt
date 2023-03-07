package com.dreamsoftware.artcollectibles.domain.usecase.impl

import com.dreamsoftware.artcollectibles.data.api.repository.IArtCollectibleRepository
import com.dreamsoftware.artcollectibles.data.api.repository.IWalletRepository
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectible
import com.dreamsoftware.artcollectibles.domain.models.CreateArtCollectibleMetadata
import com.dreamsoftware.artcollectibles.domain.usecase.core.BaseUseCaseWithParams

class CreateArtCollectibleUseCase(
    private val artCollectibleRepository: IArtCollectibleRepository,
    private val walletRepository: IWalletRepository
): BaseUseCaseWithParams<CreateArtCollectibleUseCase.Params, ArtCollectible>() {

    override suspend fun onExecuted(params: Params): ArtCollectible = with(params) {
        val credentials = walletRepository.loadCredentials()
        artCollectibleRepository.create(CreateArtCollectible(
            royalty = royalty,
            metadata = CreateArtCollectibleMetadata(
                name = name,
                description = description,
                fileUri = imagePath,
                mediaType = "image/$mediaType",
                tags = tags,
                categoryUid = categoryUid,
                authorAddress = credentials.address
            )
        ))
    }

    data class Params(
        val imagePath: String,
        val mediaType: String,
        val name: String,
        val description: String? = null,
        val royalty: Long,
        val categoryUid: String,
        val tags: List<String>
    )
}