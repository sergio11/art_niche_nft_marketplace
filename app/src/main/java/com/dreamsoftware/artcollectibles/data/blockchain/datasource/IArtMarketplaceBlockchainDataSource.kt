package com.dreamsoftware.artcollectibles.data.blockchain.datasource

import com.dreamsoftware.artcollectibles.data.blockchain.exception.*
import com.dreamsoftware.artcollectibles.data.blockchain.model.*
import org.web3j.crypto.Credentials
import java.math.BigInteger

interface IArtMarketplaceBlockchainDataSource {

    /**
     * Fetch non sold and non canceled market items
     * @param credentials
     * @param limit
     */
    @Throws(FetchAvailableMarketItemsException::class)
    suspend fun fetchAvailableMarketItems(credentials: Credentials, limit: Int? = null): Iterable<ArtCollectibleForSaleDTO>

    /**
     * Fetch market items that are being listed by the current authenticated user
     * @param credentials
     * @param limit
     */
    @Throws(FetchSellingMarketItemsException::class)
    suspend fun fetchSellingMarketItems(credentials: Credentials, limit: Int? = null): Iterable<ArtCollectibleForSaleDTO>

    /**
     * Fetch market items that are owned by the current authenticated user
     * @param credentials
     * @param limit
     */
    @Throws(FetchOwnedMarketItemsException::class)
    suspend fun fetchOwnedMarketItems(credentials: Credentials, limit: Int? = null): Iterable<ArtCollectibleForSaleDTO>

    /**
     * Allow us to fetch market history
     * @param credentials
     * @param limit
     */
    @Throws(FetchMarketHistoryException::class)
    suspend fun fetchMarketHistory(credentials: Credentials, limit: Int? = null): Iterable<ArtCollectibleForSaleDTO>

    /**
     * list an item with a `tokenId` for a `priceInEth`
     * @param tokenId
     * @param priceInEth
     * @param credentials
     */
    @Throws(PutItemForSaleException::class)
    suspend fun putItemForSale(tokenId: BigInteger, priceInEth: Float, credentials: Credentials)

    /**
     * Cancel a listing of an item with a `tokenId`
     * @param tokenId
     * @param credentials
     */
    @Throws(WithdrawFromSaleException::class)
    suspend fun withdrawFromSale(tokenId: BigInteger, credentials: Credentials)

    /**
     * Buy an item with a `tokenId` and pay the owner and the creator
     * @param tokenId
     * @param credentials
     */
    @Throws(BuyItemException::class)
    suspend fun buyItem(tokenId: BigInteger, credentials: Credentials)

    /**
     * Fetch item for sale
     * @param tokenId
     * @param credentials
     */
    @Throws(FetchItemForSaleException::class)
    suspend fun fetchItemForSale(tokenId: BigInteger, credentials: Credentials): ArtCollectibleForSaleDTO

    /**
     * Fetch market history item
     * @param marketItemId
     * @param credentials
     */
    @Throws(FetchMarketHistoryItemException::class)
    suspend fun fetchMarketHistoryItem(marketItemId: BigInteger, credentials: Credentials): ArtCollectibleForSaleDTO

    /**
     * Fetch item for sale by metadata CID
     * @param cid
     * @param credentials
     */
    @Throws(FetchItemForSaleByMetadataCIDException::class)
    suspend fun fetchItemForSaleByMetadataCID(cid: String, credentials: Credentials): ArtCollectibleForSaleDTO

    /**
     * Is token added for sale
     * @param tokenId
     * @param credentials
     */
    @Throws(CheckTokenAddedForSaleException::class)
    suspend fun isTokenAddedForSale(tokenId: BigInteger, credentials: Credentials): Boolean

    /**
     * Fetch Marketplace statistics
     * @param credentials
     */
    @Throws(FetchMarketplaceStatisticsException::class)
    suspend fun fetchMarketplaceStatistics(credentials: Credentials): MarketplaceStatisticsDTO

    /**
     * Fetch Wallet Statistics
     * @param credentials
     * @param ownerAddress
     */
    @Throws(FetchWalletStatisticsException::class)
    suspend fun fetchWalletStatistics(credentials: Credentials, ownerAddress: String): WalletStatisticsDTO

    /**
     * Check if token CID added for sale
     * @param cid
     * @param credentials
     */
    @Throws(CheckTokenAddedForSaleException::class)
    suspend fun isTokenCIDAddedForSale(cid: String, credentials: Credentials): Boolean

    /**
     * Fetch token market history
     * @param tokenId
     * @param limit
     * @param credentials
     */
    @Throws(FetchTokenMarketHistoryException::class)
    suspend fun fetchTokenMarketHistory(tokenId: BigInteger, credentials: Credentials, limit: Int? = null): Iterable<ArtCollectibleForSaleDTO>

    /**
     * Fetch current item price
     * @param tokenId
     * @param credentials
     */
    @Throws(FetchCurrentItemPriceException::class)
    suspend fun fetchCurrentItemPrice(tokenId: BigInteger, credentials: Credentials): ArtCollectibleForSalePricesDTO

    /**
     * Fetch token market history prices
     * @param tokenId
     * @param credentials
     */
    @Throws(FetchTokenMarketHistoryPricesException::class)
    suspend fun fetchTokenMarketHistoryPrices(tokenId: BigInteger, credentials: Credentials): Iterable<ArtCollectibleMarketHistoryPriceDTO>
}