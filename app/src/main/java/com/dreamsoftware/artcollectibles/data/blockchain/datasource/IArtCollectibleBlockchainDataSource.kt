package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleBlockchainDTO
import com.dreamsoftware.artcollectibles.data.blockchain.model.ArtCollectibleMintedEventDTO
import io.reactivex.Flowable
import org.web3j.crypto.Credentials
import java.math.BigInteger

interface IArtCollectibleBlockchainDataSource {

    /**
     * Observe Art Collectible Minted events
     */
    suspend fun observeArtCollectibleMintedEvents(credentials: Credentials): Flowable<ArtCollectibleMintedEventDTO>

    /**
     * Allow us to mint a new token
     */
    suspend fun mintToken(metadataCid: String, royalty: Long, credentials: Credentials): BigInteger

    /**
     * Allow us to burn a token
     */
    suspend fun burnToken(tokenId: BigInteger, credentials: Credentials)

    /**
     * Allows you to retrieve the list of tokens created
     */
    suspend fun getTokensCreated(credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Allows you to retrieve the list of tokens created by the creator address
     */
    suspend fun getTokensCreatedBy(credentials: Credentials, creatorAddress: String): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Allows you to retrieve the list of tokens owned
     */
    suspend fun getTokensOwned(credentials: Credentials): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Allows you to retrieve the list of tokens owned by the owner address
     */
    suspend fun getTokensOwnedBy(credentials: Credentials, ownerAddress: String): Iterable<ArtCollectibleBlockchainDTO>

    /**
     * Retrieve token information by id
     */
    suspend fun getTokenById(tokenId: BigInteger, credentials: Credentials): ArtCollectibleBlockchainDTO
}