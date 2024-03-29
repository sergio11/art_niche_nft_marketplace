package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IVisitorsDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.internal.toLongOrDefault
import java.math.BigInteger

/**
 * Visitors Data Source
 * @param firebaseStore
 */
internal class VisitorsDataSourceImpl(
    private val firebaseStore: FirebaseFirestore
): IVisitorsDataSource {

    private companion object {
        const val COLLECTION_NAME = "visitors"
        const val COUNT_FIELD_NAME = "count"
        const val IDS_FIELD_NAME = "ids"
        const val KEY_COUNT_SUFFIX = "_count"
    }

    @Throws(AddVisitorException::class)
    override suspend fun addVisitor(tokenId: BigInteger, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(tokenId.toString()).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayUnion(userAddress)), SetOptions.merge()).await()
                    document(tokenId.toString() + KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)), SetOptions.merge()).await()
                }
            }  catch (ex: Exception) {
                throw AddVisitorException("An error occurred when trying to add visitor", ex)
            }
        }
    }

    @Throws(GetVisitorsCountException::class)
    override suspend fun count(tokenId: BigInteger) = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(tokenId.toString() + KEY_COUNT_SUFFIX)
                .get()
                .await()?.data?.get(COUNT_FIELD_NAME)
                .toString()
                .toLongOrDefault(0L)
        } catch (ex: Exception) {
            throw GetVisitorsCountException("An error occurred when trying to get visitors count", ex)
        }
    }

    @Throws(GetVisitorsByTokenException::class)
    override suspend fun getByTokenId(tokenId: BigInteger): List<String> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(tokenId.toString())
                .get()
                .await()?.data?.get(IDS_FIELD_NAME) as? List<String> ?: emptyList()
        } catch (ex: Exception) {
            throw GetVisitorsByTokenException("An error occurred when trying to get visitors", ex)
        }
    }

    @Throws(GetMostVisitedTokensException::class)
    override suspend fun getMostVisitedTokens(limit: Int): List<String> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .orderBy(COUNT_FIELD_NAME, Query.Direction.DESCENDING)
                .limit(limit.toLong()).get()
                .await()?.documents?.mapNotNull { it.id }
                ?.filter { it.contains(KEY_COUNT_SUFFIX) }
                ?.map { it.removeSuffix(KEY_COUNT_SUFFIX) }.orEmpty()
        } catch (ex: Exception) {
            throw GetMostVisitedTokensException("An error occurred when trying to get most visited tokens", ex)
        }
    }
}