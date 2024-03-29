package com.dreamsoftware.artcollectibles.ui.screens.tokendetail

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.ArtCollectibleForSale
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.ui.components.*
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.dreamsoftware.artcollectibles.ui.theme.DarkPurple
import com.dreamsoftware.artcollectibles.ui.theme.Purple500
import com.dreamsoftware.artcollectibles.ui.theme.Purple700
import com.google.common.collect.Iterables
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import java.math.BigInteger

data class TokenDetailScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun TokenDetailScreen(
    args: TokenDetailScreenArgs,
    viewModel: TokenDetailViewModel = hiltViewModel(),
    onTokenBurned: () -> Unit,
    onGoToArtistDetail: (userUid: String) -> Unit,
    onGoToCommentDetail: (comment: Comment) -> Unit,
    onGoToCommentsByToken: (tokenId: BigInteger) -> Unit,
    onGoToLikesByToken: (tokenId: BigInteger) -> Unit,
    onGoToVisitorsByToken: (tokenId: BigInteger) -> Unit,
    onGoToTokenHistory: (tokenId: BigInteger) -> Unit,
    onGoToMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onGoToMarketHistoryItemDetail: (marketItemId: BigInteger) -> Unit,
    onGoToEditToken: (metadataCid: String) -> Unit,
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = TokenDetailUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.uiState.collect {
                if (it.isBurned) {
                    onTokenBurned()
                } else {
                    value = it
                }
            }
        }
    }
    val density = LocalDensity.current
    val scrollState: ScrollState = rememberScrollState(0)
    val snackBarHostState = remember { SnackbarHostState() }
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadDetail(tokenId = args.tokenId)
        }
        TokenDetailComponent(
            context = context,
            uiState = uiState,
            snackBarHostState = snackBarHostState,
            scrollState = scrollState,
            density = density,
            onGoToArtistDetail = onGoToArtistDetail,
            onBackClicked = onBackClicked,
            onBurnTokenCalled = ::burnToken,
            onPutItemForSaleCalled = ::putItemForSale,
            onWithDrawFromSaleCalled = ::withDrawFromSale,
            onItemPriceChanged = ::onTokenPriceChanged,
            onTokenAddedToFavorites = ::addTokenToFavorites,
            onTokenRemovedFromFavorites = ::removeTokenFromFavorites,
            onConfirmBurnTokenDialogVisibilityChanged = ::onConfirmBurnTokenDialogVisibilityChanged,
            onConfirmWithDrawFromSaleDialogVisibilityChanged = ::onConfirmWithDrawFromSaleDialogVisibilityChanged,
            onConfirmPutForSaleDialogVisibilityChanged = ::onConfirmPutForSaleDialogVisibilityChanged,
            onPublishComment = ::onPublishComment,
            onGoToCommentDetail = onGoToCommentDetail,
            onGoToCommentsByToken = onGoToCommentsByToken,
            onGoToLikesByToken = onGoToLikesByToken,
            onGoToVisitorsByToken = onGoToVisitorsByToken,
            onGoToTokenHistory = onGoToTokenHistory,
            onGoToMarketItemDetail = onGoToMarketItemDetail,
            onGoToTokenDetail = onGoToTokenDetail,
            onGoToEditToken = onGoToEditToken,
            onGoToMarketHistoryItemDetail = onGoToMarketHistoryItemDetail
        )
    }
}

@Composable
fun TokenDetailComponent(
    context: Context,
    uiState: TokenDetailUiState,
    snackBarHostState: SnackbarHostState,
    scrollState: ScrollState,
    density: Density,
    onBackClicked: () -> Unit,
    onGoToArtistDetail: (userUid: String) -> Unit,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onTokenAddedToFavorites: (tokenId: BigInteger) -> Unit,
    onTokenRemovedFromFavorites: (tokenId: BigInteger) -> Unit,
    onConfirmBurnTokenDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmWithDrawFromSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmPutForSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onItemPriceChanged: (price: Float) -> Unit,
    onPublishComment: (comment: String) -> Unit,
    onGoToCommentDetail: (comment: Comment) -> Unit,
    onGoToCommentsByToken: (tokenId: BigInteger) -> Unit,
    onGoToLikesByToken: (tokenId: BigInteger) -> Unit,
    onGoToVisitorsByToken: (tokenId: BigInteger) -> Unit,
    onGoToTokenHistory: (tokenId: BigInteger) -> Unit,
    onGoToMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onGoToEditToken: (metadataCid: String) -> Unit,
    onGoToMarketHistoryItemDetail: (marketItemId: BigInteger) -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            snackBarHostState = snackBarHostState,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            onBackClicked = onBackClicked,
            contentCentered = true,
            imageUrl = artCollectible?.metadata?.imageUrl,
            title = artCollectible?.displayName
        ) {
            TokenDetailBody(
                context = context,
                uiState = uiState,
                onGoToArtistDetail = onGoToArtistDetail,
                onBurnTokenCalled = onBurnTokenCalled,
                onWithDrawFromSaleCalled = onWithDrawFromSaleCalled,
                onPutItemForSaleCalled = onPutItemForSaleCalled,
                onItemPriceChanged = onItemPriceChanged,
                onTokenAddedToFavorites = onTokenAddedToFavorites,
                onTokenRemovedFromFavorites = onTokenRemovedFromFavorites,
                onConfirmBurnTokenDialogVisibilityChanged = onConfirmBurnTokenDialogVisibilityChanged,
                onConfirmWithDrawFromSaleDialogVisibilityChanged = onConfirmWithDrawFromSaleDialogVisibilityChanged,
                onConfirmPutForSaleDialogVisibilityChanged = onConfirmPutForSaleDialogVisibilityChanged,
                onPublishComment = onPublishComment,
                onGoToCommentDetail = onGoToCommentDetail,
                onGoToAllComments = onGoToCommentsByToken,
                onGoToLikesByToken = onGoToLikesByToken,
                onGoToVisitorsByToken = onGoToVisitorsByToken,
                onGoToTokenHistory = onGoToTokenHistory,
                onGoToMarketItemDetail = onGoToMarketItemDetail,
                onGoToTokenDetail = onGoToTokenDetail,
                onGoToEditToken = onGoToEditToken,
                onGoToMarketHistoryItemDetail = onGoToMarketHistoryItemDetail
            )
        }
    }
}


@Composable
private fun TokenDetailBody(
    context: Context,
    uiState: TokenDetailUiState,
    onGoToArtistDetail: (userUid: String) -> Unit,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onItemPriceChanged: (price: Float) -> Unit,
    onTokenAddedToFavorites: (tokenId: BigInteger) -> Unit,
    onTokenRemovedFromFavorites: (tokenId: BigInteger) -> Unit,
    onConfirmBurnTokenDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmWithDrawFromSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmPutForSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onPublishComment: (comment: String) -> Unit,
    onGoToCommentDetail: (comment: Comment) -> Unit,
    onGoToAllComments: (tokenId: BigInteger) -> Unit,
    onGoToLikesByToken: (tokenId: BigInteger) -> Unit,
    onGoToVisitorsByToken: (tokenId: BigInteger) -> Unit,
    onGoToTokenHistory: (tokenId: BigInteger) -> Unit,
    onGoToMarketItemDetail: (tokenId: BigInteger) -> Unit,
    onGoToTokenDetail: (tokenId: BigInteger) -> Unit,
    onGoToEditToken: (metadataCid: String) -> Unit,
    onGoToMarketHistoryItemDetail: (marketItemId: BigInteger) -> Unit
) {
    with(uiState) {
        artCollectible?.let { artCollectible ->
            //.......................................................................
            ConfirmBurnTokenDialog(uiState, onBurnTokenCalled) {
                onConfirmBurnTokenDialogVisibilityChanged(false)
            }
            ConfirmWithdrawFromSaleDialog(uiState, onWithDrawFromSaleCalled) {
                onConfirmWithDrawFromSaleDialogVisibilityChanged(false)
            }
            ConfirmPutItemForSaleDialog(uiState, onPutItemForSaleCalled, onItemPriceChanged) {
                onConfirmPutForSaleDialogVisibilityChanged(false)
            }
            //.......................................................................
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                UserMiniInfoComponent(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            onGoToArtistDetail(artCollectible.owner.uid)
                        },
                    userInfo = artCollectible.owner
                )
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    if(isTokenOwner) {
                        EditButton(
                            modifier = Modifier.size(54.dp),
                            onEditClicked = {
                                onGoToEditToken(artCollectible.metadata.cid)
                            }
                        )
                    }
                    FavoriteButton(
                        modifier = Modifier.size(54.dp),
                        isChecked = tokenAddedToFavorites,
                        onCheckedChange = {
                            if (tokenAddedToFavorites) {
                                onTokenRemovedFromFavorites(artCollectible.id)
                            } else {
                                onTokenAddedToFavorites(artCollectible.id)
                            }
                        }
                    )
                }
            }
            if (isTokenAddedForSale) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { onGoToMarketItemDetail(artCollectible.id) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(10.dp),
                        painter = painterResource(id = R.drawable.available_for_sale),
                        contentDescription = "Token Image"
                    )
                    Column {
                        CommonText(
                            type = CommonTextTypeEnum.BODY_LARGE,
                            titleRes = R.string.token_detail_available_for_sale_text,
                            singleLine = true
                        )
                        tokenCurrentPrices?.let {
                            ArtCollectiblePrice(
                                iconSize = 20.dp,
                                textSize = 20.sp,
                                textColor = DarkPurple,
                                fullMode = true,
                                priceData = it
                            )
                        }
                    }
                }
            }
            ArtCollectibleMiniInfoComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                artCollectible = artCollectible,
                onSeeAllComments = onGoToAllComments,
                onSeeLikesByToken = onGoToLikesByToken,
                onSeeVisitorsByToken = onGoToVisitorsByToken,
                onSeeCreatorDetail = onGoToArtistDetail
            )
            TokenTags(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                tags = artCollectible.metadata.tags
            )
            TokenMarketHistory(
                modifier = Modifier.padding(8.dp),
                tokenMarketHistory = lastMarketHistory,
                onGoToMarketHistoryItemDetail = onGoToMarketHistoryItemDetail,
                onSeeAllTokenHistory = {
                    onGoToTokenHistory(artCollectible.id)
                }
            )
            tokenMarketHistoryChartEntryModel?.let {
                TokenPricesChart(
                    modifier = Modifier.padding(8.dp),
                    chartEntryModel = it
                )
            }
            PublishCommentComponent(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                authUserInfo = authUserInfo,
                commentsCount = artCollectible.commentsCount,
                lastComments = lastComments,
                enabled = allowToPublishComments,
                onPublishComment = onPublishComment,
                onSeeCommentDetail = onGoToCommentDetail,
                onSeeAllComments = {
                    onGoToAllComments(artCollectible.id)
                }
            )
            ArtCollectiblesRow(
                context = context,
                reverseStyle = true,
                titleRes = R.string.token_detail_similar_row_title_text,
                items = similarTokens,
                onItemSelected = {
                    onGoToTokenDetail(it.id)
                }
            )
            Spacer(modifier = Modifier.height(50.dp))
            if (isTokenAddedForSale) {
                CommonButton(
                    text = R.string.token_detail_go_market_button_text,
                    containerColor = Purple700,
                    contentColor = Color.White,
                    onClick = {
                        onGoToMarketItemDetail(artCollectible.id)
                    }
                )
            }
            if (isTokenOwner) {
                CommonButton(
                    text = if (isTokenAddedForSale) {
                        R.string.token_detail_with_draw_from_sale_button_text
                    } else {
                        R.string.token_detail_put_item_for_sale_button_text
                    },
                    containerColor = if (isTokenAddedForSale) {
                        Color.Red
                    } else {
                        Purple500
                    },
                    contentColor = Color.White,
                    onClick = {
                        if (isTokenAddedForSale) {
                            onConfirmWithDrawFromSaleDialogVisibilityChanged(true)
                        } else {
                            onConfirmPutForSaleDialogVisibilityChanged(true)
                        }
                    }
                )
                CommonButton(
                    text = R.string.token_detail_burn_token_button_text,
                    enabled = !isTokenAddedForSale,
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    onClick = {
                        onConfirmBurnTokenDialogVisibilityChanged(true)
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


@Composable
private fun ConfirmWithdrawFromSaleDialog(
    uiState: TokenDetailUiState,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onDialogCancelled: () -> Unit
) {
    with(uiState) {
        CommonDialog(
            isVisible = isConfirmWithDrawFromSaleDialogVisible,
            titleRes = R.string.token_detail_with_draw_from_sale_dialog_title_text,
            descriptionRes = R.string.token_detail_with_draw_from_sale_dialog_description_text,
            acceptRes = R.string.token_detail_with_draw_from_sale_dialog_accept_button_text,
            cancelRes = R.string.token_detail_with_draw_from_sale_dialog_cancel_button_text,
            onAcceptClicked = {
                artCollectible?.let {
                    onWithDrawFromSaleCalled(it.id)
                }
            },
            onCancelClicked = onDialogCancelled
        )
    }
}

@Composable
private fun ConfirmBurnTokenDialog(
    uiState: TokenDetailUiState,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onDialogCancelled: () -> Unit
) {
    with(uiState) {
        CommonDialog(
            isVisible = isConfirmBurnTokenDialogVisible,
            titleRes = R.string.token_detail_burn_token_confirm_title_text,
            descriptionRes = R.string.token_detail_burn_token_confirm_description_text,
            acceptRes = R.string.token_detail_burn_token_confirm_accept_button_text,
            cancelRes = R.string.token_detail_burn_token_confirm_cancel_button_text,
            onAcceptClicked = {
                artCollectible?.let {
                    onBurnTokenCalled(it.id)
                }
            },
            onCancelClicked = onDialogCancelled
        )
    }
}

@Composable
private fun ConfirmPutItemForSaleDialog(
    uiState: TokenDetailUiState,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onItemPriceChanged: (priceInEth: Float) -> Unit,
    onDialogCancelled: () -> Unit
) {
    with(uiState) {
        CommonDialog(
            isVisible = isConfirmPutForSaleDialogVisible,
            titleRes = R.string.token_detail_put_item_for_sale_dialog_title_text,
            descriptionRes = R.string.token_detail_put_item_for_sale_dialog_description_text,
            acceptRes = R.string.token_detail_put_item_for_sale_dialog_accept_button_text,
            cancelRes = R.string.token_detail_put_item_for_sale_dialog_cancel_button_text,
            onAcceptClicked = {
                artCollectible?.let {
                    onPutItemForSaleCalled(it.id)
                }
            },
            isAcceptEnabled = isPutTokenForSaleConfirmButtonEnabled,
            onCancelClicked = onDialogCancelled
        ) {
            CommonDefaultDecimalField(
                modifier = Modifier.padding(horizontal = 8.dp),
                labelRes = R.string.token_detail_put_item_for_sale_input_price_label,
                placeHolderRes = R.string.token_detail_put_item_for_sale_input_price_placeholder,
                value = tokenPriceInEth,
                leadingIcon = {
                    MaticIconComponent(size = 20.dp)
                },
                onValueChanged = {
                    onItemPriceChanged(it)
                }
            )
        }
    }
}

@Composable
private fun TokenTags(
    modifier: Modifier = Modifier,
    tags: List<String>
) {
    Column(
        modifier = modifier
    ) {
        CommonText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            type = CommonTextTypeEnum.TITLE_LARGE,
            titleRes = R.string.token_detail_tags_title_text,
            singleLine = true
        )
        TagsRow(
            tagList = tags,
            isReadOnly = true
        )
    }
}

@Composable
private fun TokenMarketHistory(
    modifier: Modifier = Modifier,
    tokenMarketHistory: Iterable<ArtCollectibleForSale>,
    onGoToMarketHistoryItemDetail: (marketItemId: BigInteger) -> Unit,
    onSeeAllTokenHistory: (() -> Unit)? = null
) {
    if(Iterables.size(tokenMarketHistory) > 0) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CommonText(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    type = CommonTextTypeEnum.TITLE_LARGE,
                    titleRes = R.string.token_detail_last_transactions_title_text,
                    singleLine = true
                )
                onSeeAllTokenHistory?.let {
                    Image(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(35.dp)
                            .clickable { it() },
                        painter = painterResource(R.drawable.arrow_right_icon),
                        contentDescription = "onSeeAllTokenHistory",
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(DarkPurple)
                    )
                }
            }
            repeat(Iterables.size(tokenMarketHistory)) {
                with(Iterables.get(tokenMarketHistory, it)) {
                    TokenTransactionItem(
                        modifier = Modifier.clickable { onGoToMarketHistoryItemDetail(marketItemId) },
                        item = this
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun TokenPricesChart(
    modifier: Modifier = Modifier,
    chartEntryModel: ChartEntryModel
) {
    if(chartEntryModel.entries.isNotEmpty()) {
        CommonChart(
            modifier = modifier,
            titleRes = R.string.token_detail_price_history_title_text,
            entryModel = chartEntryModel,
            bottomAxisValueFormatter = { value, chartValues ->
                (chartValues.chartEntryModel.entries.first()
                    .getOrNull(value.toInt()) as? TokenPriceChartEntry)
                    ?.dateText
                    .orEmpty()
            }
        )
    }
}