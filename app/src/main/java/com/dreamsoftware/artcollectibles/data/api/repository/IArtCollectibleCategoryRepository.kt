package com.dreamsoftware.artcollectibles.data.api.repository

import com.dreamsoftware.artcollectibles.data.api.exception.GetCategoriesDataException
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleCategory

interface IArtCollectibleCategoryRepository {

    @Throws(GetCategoriesDataException::class)
    suspend fun getAll(): Iterable<ArtCollectibleCategory>
}