package com.dreamsoftware.artcollectibles.ui.screens.categorydetail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.ui.components.ArtCollectibleForSaleCard
import com.dreamsoftware.artcollectibles.ui.components.CommonDetailScreen
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.common.collect.Iterables
import java.math.BigInteger

data class CategoryDetailScreenArgs(
    val uid: String
)

@Composable
fun CategoryDetailScreen(
    args: CategoryDetailScreenArgs,
    viewModel: CategoryDetailViewModel = hiltViewModel(),
    onShowTokenForSaleDetail: (tokenId: BigInteger) -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = CategoryDetailUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                value = it
            }
        }
    }
    val density = LocalDensity.current
    val scrollState: ScrollState = rememberScrollState(0)
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load(args.uid)
        }
        CategoryDetailComponent(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            density = density,
            onTokenForSaleClicked = onShowTokenForSaleDetail
        )
    }
}

@Composable
fun CategoryDetailComponent(
    context: Context,
    uiState: CategoryDetailUiState,
    scrollState: ScrollState,
    density: Density,
    onTokenForSaleClicked: (tokenId: BigInteger) -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            imageUrl = uiState.category?.imageUrl,
            title = uiState.category?.name?.ifBlank {
                stringResource(id = R.string.no_text_value)
            } ?: stringResource(id = R.string.no_text_value)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 4.dp),
                crossAxisAlignment = FlowCrossAxisAlignment.Center,
                mainAxisAlignment = MainAxisAlignment.Center,
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp
            ) {
                repeat(Iterables.size(tokensForSale)) {
                    with(Iterables.get(tokensForSale, it)) {
                        ArtCollectibleForSaleCard(context = context, reverseStyle = true, artCollectibleForSale = this) {
                            onTokenForSaleClicked(token.id)
                        }
                    }
                }
            }
        }
    }
}