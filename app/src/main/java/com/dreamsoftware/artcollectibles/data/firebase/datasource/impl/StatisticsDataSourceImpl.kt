package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IStatisticsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.FetchMarketStatisticsException
import com.dreamsoftware.artcollectibles.data.firebase.exception.RegisterEventException
import com.dreamsoftware.artcollectibles.data.firebase.model.MarketStatisticDTO
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.math.BigInteger

internal class StatisticsDataSourceImpl(
    private val firebaseStore: FirebaseFirestore
): IStatisticsDataSource {

    private companion object {
        const val COLLECTION_NAME = "statistics"
        const val COUNT_PURCHASES_FIELD = "count_purchases"
        const val COUNT_SALES_FIELD = "count_sales"
        const val COUNT_CREATED_FIELD = "count_tokens_created"
        const val COUNT_TOKEN_SOLD = "count_token_sold"
        const val COUNT_TOKEN_CANCELLED = "count_token_cancelled"
    }

    @Throws(FetchMarketStatisticsException::class)
    override suspend fun fetchUsersWithMorePurchases(limit: Int): Iterable<MarketStatisticDTO> =
        fetchStatistics(field = COUNT_PURCHASES_FIELD, limit = limit)

    @Throws(FetchMarketStatisticsException::class)
    override suspend fun fetchUsersWithMoreSales(limit: Int): Iterable<MarketStatisticDTO> =
        fetchStatistics(field = COUNT_SALES_FIELD, limit = limit)

    @Throws(FetchMarketStatisticsException::class)
    override suspend fun fetchUsersWithMoreTokensCreated(limit: Int): Iterable<MarketStatisticDTO> =
        fetchStatistics(field = COUNT_CREATED_FIELD, limit = limit)

    @Throws(FetchMarketStatisticsException::class)
    override suspend fun fetchMostSoldTokens(limit: Int): Iterable<MarketStatisticDTO> =
        fetchStatistics(field = COUNT_TOKEN_SOLD, limit = limit)

    @Throws(FetchMarketStatisticsException::class)
    override suspend fun fetchMostCancelledTokens(limit: Int): Iterable<MarketStatisticDTO> =
        fetchStatistics(field = COUNT_TOKEN_CANCELLED, limit = limit)

    @Throws(RegisterEventException::class)
    override suspend fun registerNewPurchase(userUid: String) {
        registerEvent(
            key = userUid,
            field = COUNT_PURCHASES_FIELD
        )
    }

    @Throws(RegisterEventException::class)
    override suspend fun registerNewSale(userUid: String) {
        registerEvent(
            key = userUid,
            field = COUNT_SALES_FIELD
        )
    }

    @Throws(RegisterEventException::class)
    override suspend fun registerNewCreation(userUid: String) {
        registerEvent(
            key = userUid,
            field = COUNT_CREATED_FIELD
        )
    }

    @Throws(RegisterEventException::class)
    override suspend fun registerNewTokenSold(tokenId: BigInteger) {
        registerEvent(
            key = tokenId.toString(),
            field = COUNT_TOKEN_SOLD
        )
    }

    @Throws(RegisterEventException::class)
    override suspend fun registerNewTokenCancellation(tokenId: BigInteger) {
        registerEvent(
            key = tokenId.toString(),
            field = COUNT_TOKEN_CANCELLED
        )
    }

    private suspend fun registerEvent(key: String, field: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME)
                    .document(key)
                    .set(hashMapOf(field to FieldValue.increment(1)), SetOptions.merge())
                    .await()
            } catch (ex: Exception) {
                throw RegisterEventException(
                    "An error occurred when trying to register event",
                    ex
                )
            }
        }
    }

    private suspend fun fetchStatistics(field: String, limit: Int): Iterable<MarketStatisticDTO> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .orderBy(field, Query.Direction.DESCENDING)
                .limit(limit.toLong()).get()
                .await()?.documents?.mapNotNull {
                    MarketStatisticDTO(key = it.id, value = it.data?.get(field)?.toString()?.toLongOrNull() ?: 0L)
                }
                .orEmpty()
        } catch (ex: Exception) {
            throw FetchMarketStatisticsException(
                "An error occurred when trying to fetch statistics",
                ex
            )
        }
    }
}