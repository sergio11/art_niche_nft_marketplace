package com.dreamsoftware.artcollectibles.ui.screens.markethistory


import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.components.MarketHistoryMiniCard
import com.dreamsoftware.artcollectibles.ui.components.core.CommonVerticalGridScreen
import com.dreamsoftware.artcollectibles.ui.components.core.produceUiState
import com.google.common.collect.Iterables
import java.math.BigInteger

@Composable
fun MarketHistoryScreen(
    viewModel: MarketHistoryViewModel = hiltViewModel(),
    onGoToMarketItemHistoryDetail: (marketItemId: BigInteger) -> Unit,
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceUiState(
        initialState = MarketHistoryUiState(),
        lifecycle = lifecycle,
        viewModel = viewModel
    )
    val snackBarHostState = remember { SnackbarHostState() }
    val lazyGridState = rememberLazyGridState()
    val context = LocalContext.current
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load()
        }
        MarketHistoryComponent(
            context = context,
            snackBarHostState = snackBarHostState,
            state = uiState,
            lazyGridState = lazyGridState,
            onRetryCalled = ::load,
            onBackPressed = onBackPressed,
            onGoToMarketItemHistoryDetail = onGoToMarketItemHistoryDetail
        )
    }
}

@Composable
internal fun MarketHistoryComponent(
    context: Context,
    snackBarHostState: SnackbarHostState,
    state: MarketHistoryUiState,
    lazyGridState: LazyGridState,
    onRetryCalled: () -> Unit,
    onBackPressed: () -> Unit,
    onGoToMarketItemHistoryDetail: (marketItemId: BigInteger) -> Unit
) {
    with(state) {
        CommonVerticalGridScreen(
            lazyGridState = lazyGridState,
            snackBarHostState = snackBarHostState,
            isLoading = isLoading,
            items = marketItems,
            noDataFoundMessageId = R.string.market_history_not_found_message,
            onRetryCalled = onRetryCalled,
            onBackPressed = onBackPressed,
            appBarTitle = getTopAppBarTitle(marketItems)
        ) { marketItem ->
            MarketHistoryMiniCard(
                context = context,
                artCollectibleForSale = marketItem,
                onClicked = {
                    onGoToMarketItemHistoryDetail(marketItem.marketItemId)
                }
            )
        }
    }
}

@Composable
private fun getTopAppBarTitle(
    items: Iterable<ArtCollectibleForSale>
) = if(Iterables.isEmpty(items)) {
    stringResource(id = R.string.market_history_title_default)
} else {
    stringResource(id = R.string.market_history_title_count, Iterables.size(items))
}