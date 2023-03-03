package com.dreamsoftware.artcollectibles.ui.screens.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.MarketplaceStatistics
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchAvailableMarketItemsUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchMarketHistoryUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchMarketplaceStatisticsUseCase
import com.dreamsoftware.artcollectibles.domain.usecase.impl.FetchSellingMarketItemsUseCase
import com.dreamsoftware.artcollectibles.ui.screens.core.SupportViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchAvailableMarketItemsUseCase: FetchAvailableMarketItemsUseCase,
    private val fetchSellingMarketItemsUseCase: FetchSellingMarketItemsUseCase,
    private val fetchMarketplaceStatisticsUseCase: FetchMarketplaceStatisticsUseCase,
    private val fetchMarketHistoryUseCase: FetchMarketHistoryUseCase
) : SupportViewModel<HomeUiState>() {

    override fun onGetDefaultState(): HomeUiState = HomeUiState()

    fun loadData() {
        onLoading()
        fetchMarketplaceStatistics()
        fetchAvailableMarketItems()
        fetchSellingMarketItems()
        fetchMarketHistory()
    }

    private fun fetchMarketplaceStatistics() {
        fetchMarketplaceStatisticsUseCase.invoke(
            scope = viewModelScope,
            onSuccess = { marketplaceStatistics ->
                updateState {
                    it.copy(
                        isLoading = false,
                        marketplaceStatistics = marketplaceStatistics
                    )
                }
            },
            onError = {}
        )
    }

    private fun fetchAvailableMarketItems() {
        fetchAvailableMarketItemsUseCase.invoke(
            scope = viewModelScope,
            onSuccess = { availableMarketItems ->
                updateState {
                    it.copy(availableMarketItems = availableMarketItems)
                }
            },
            onError = {}
        )
    }

    private fun fetchSellingMarketItems() {
        fetchSellingMarketItemsUseCase.invoke(
            scope = viewModelScope,
            onSuccess = { sellingItems ->
                updateState {
                    it.copy(sellingMarketItems = sellingItems)
                }
            },
            onError = {}
        )
    }

    private fun fetchMarketHistory() {
        fetchMarketHistoryUseCase.invoke(
            scope = viewModelScope,
            onSuccess = { marketHistory ->
                updateState {
                    it.copy(marketHistory = marketHistory)
                }
            },
            onError = {})
    }

    private fun onLoading() {
        updateState { it.copy(isLoading = true) }
    }

    private fun onErrorOccurred(ex: Exception) {
        ex.printStackTrace()
        Log.d("ART_COLL", "onErrorOccurred ${ex.message} CALLED!")
        updateState { it.copy(isLoading = false) }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val marketplaceStatistics: MarketplaceStatistics? = null,
    val availableMarketItems: Iterable<ArtCollectibleForSale> = emptyList(),
    val sellingMarketItems: Iterable<ArtCollectibleForSale> = emptyList(),
    val marketHistory: Iterable<ArtCollectibleForSale> = emptyList()
)