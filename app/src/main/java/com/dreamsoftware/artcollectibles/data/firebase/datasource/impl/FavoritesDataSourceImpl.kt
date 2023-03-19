package com.dreamsoftware.artcollectibles.data.firebase.datasource.impl

import com.dreamsoftware.artcollectibles.data.firebase.datasource.IFavoritesDataSource
import com.dreamsoftware.artcollectibles.data.firebase.exception.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.internal.toLongOrDefault

internal class FavoritesDataSourceImpl(
    private val firebaseStore: FirebaseFirestore
): IFavoritesDataSource {

    private companion object {
        const val COLLECTION_NAME = "favorites"
        const val COUNT_FIELD_NAME = "count"
        const val IDS_FIELD_NAME = "ids"
        const val USER_KEY_COUNT_SUFFIX = "_user_count"
        const val TOKEN_KEY_COUNT_SUFFIX = "_token_count"
    }

    @Throws(GetFavoritesException::class)
    override suspend fun hasAdded(tokenId: String, userAddress: String): Boolean = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .whereArrayContains(IDS_FIELD_NAME, tokenId)
                .get()
                .await()
                .documents.mapNotNull { it.id }
                .contains(userAddress)
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFavoritesException("An error occurred when trying to get favorites", ex)
        }
    }

    @Throws(GetFavoritesException::class)
    override suspend fun getList(userAddress: String): List<String> = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(userAddress)
                .get()
                .await()?.data?.get(IDS_FIELD_NAME) as? List<String> ?: emptyList()
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFavoritesException("An error occurred when trying to get favorites", ex)
        }
    }

    @Throws(GetFavoritesException::class)
    override suspend fun tokenCount(tokenId: String): Long = withContext(Dispatchers.IO) {
        try {
            firebaseStore.collection(COLLECTION_NAME)
                .document(tokenId + TOKEN_KEY_COUNT_SUFFIX)
                .get()
                .await()?.data?.get(COUNT_FIELD_NAME)
                .toString()
                .toLongOrDefault(0L)
        } catch (ex: FirebaseException) {
            throw ex
        } catch (ex: Exception) {
            throw GetFavoritesException("An error occurred when trying to get favorites", ex)
        }
    }

    @Throws(AddToFavoritesException::class)
    override suspend fun add(tokenId: String, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(tokenId).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayUnion(userAddress)), SetOptions.merge()).await()
                    document(tokenId + TOKEN_KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)), SetOptions.merge()).await()
                    document(userAddress).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayUnion(tokenId)), SetOptions.merge()).await()
                    document(userAddress + USER_KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(1)), SetOptions.merge()).await()
                }
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw AddToFavoritesException("An error occurred when trying to save favorite", ex)
            }
        }
    }

    @Throws(RemoveFromFavoritesException::class)
    override suspend fun remove(tokenId: String, userAddress: String) {
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME).apply {
                    document(tokenId).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayRemove(userAddress)), SetOptions.merge()).await()
                    document(tokenId + TOKEN_KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(-1)), SetOptions.merge()).await()
                    document(userAddress).set(hashMapOf(IDS_FIELD_NAME to FieldValue.arrayRemove(tokenId)), SetOptions.merge()).await()
                    document(userAddress + USER_KEY_COUNT_SUFFIX).set(hashMapOf(COUNT_FIELD_NAME to FieldValue.increment(-1)), SetOptions.merge()).await()
                }
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw RemoveFromFavoritesException("An error occurred when trying to remove from favorites", ex)
            }
        }
    }

    @Throws(GetMoreLikedTokensException::class)
    override suspend fun getMoreLikedTokens(limit: Long): List<String> =
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME)
                    .orderBy(COUNT_FIELD_NAME, Query.Direction.DESCENDING)
                    .limit(limit).get()
                    .await()?.documents?.mapNotNull { it.id }
                    ?.filter { it.contains(TOKEN_KEY_COUNT_SUFFIX) }
                    ?.map { it.removeSuffix(TOKEN_KEY_COUNT_SUFFIX) }.orEmpty()
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw GetMoreLikedTokensException("An error occurred when trying to get more liked tokens")
            }
        }

    @Throws(GetUserLikesByTokenException::class)
    override suspend fun getUserLikesByToken(tokenId: String): List<String> =
        withContext(Dispatchers.IO) {
            try {
                firebaseStore.collection(COLLECTION_NAME)
                    .document(tokenId)
                    .get()
                    .await()?.data?.get(IDS_FIELD_NAME) as? List<String> ?: emptyList()
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw GetUserLikesByTokenException("An error occurred when trying to get user likes by token")
            }
        }
}