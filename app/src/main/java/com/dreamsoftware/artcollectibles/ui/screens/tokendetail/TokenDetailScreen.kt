package com.dreamsoftware.artcollectibles.ui.screens.tokendetail

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.CommonButton
import com.dreamsoftware.artcollectibles.ui.components.CommonDefaultDecimalField
import com.dreamsoftware.artcollectibles.ui.components.CommonDetailScreen
import com.dreamsoftware.artcollectibles.ui.components.CommonDialog
import java.math.BigInteger

private val HEADER_HEIGHT = 250.dp
private const val PRICE_NUMBER_OF_DECIMALS = 5

data class TokenDetailScreenArgs(
    val tokenId: BigInteger
)

@Composable
fun TokenDetailScreen(
    navController: NavController,
    args: TokenDetailScreenArgs,
    viewModel: TokenDetailViewModel = hiltViewModel(),
    onTokenBurned: () -> Unit
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
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            loadDetail(tokenId = args.tokenId)
        }
        TokenDetailComponent(
            context = context,
            uiState = uiState,
            scrollState = scrollState,
            density = density,
            onBurnTokenCalled = ::burnToken,
            onPutItemForSaleCalled = ::putItemForSale,
            onWithDrawFromSaleCalled = ::withDrawFromSale,
            onItemPriceChanged = ::onTokenPriceChanged,
            onConfirmBurnTokenDialogVisibilityChanged = ::onConfirmBurnTokenDialogVisibilityChanged,
            onConfirmWithDrawFromSaleDialogVisibilityChanged = ::onConfirmWithDrawFromSaleDialogVisibilityChanged,
            onConfirmPutForSaleDialogVisibilityChanged = ::onConfirmPutForSaleDialogVisibilityChanged
        )
    }
}

@Composable
fun TokenDetailComponent(
    context: Context,
    uiState: TokenDetailUiState,
    scrollState: ScrollState,
    density: Density,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onConfirmBurnTokenDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmWithDrawFromSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmPutForSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onItemPriceChanged: (price: String) -> Unit
) {
    with(uiState) {
        CommonDetailScreen(
            context = context,
            scrollState = scrollState,
            density = density,
            isLoading = isLoading,
            imageUrl = artCollectible?.imageUrl,
            title = artCollectible?.displayName) {
            TokenDetailBody(
                scrollState = scrollState,
                uiState = uiState,
                onBurnTokenCalled = onBurnTokenCalled,
                onWithDrawFromSaleCalled = onWithDrawFromSaleCalled,
                onPutItemForSaleCalled = onPutItemForSaleCalled,
                onItemPriceChanged = onItemPriceChanged,
                onConfirmBurnTokenDialogVisibilityChanged = onConfirmBurnTokenDialogVisibilityChanged,
                onConfirmWithDrawFromSaleDialogVisibilityChanged = onConfirmWithDrawFromSaleDialogVisibilityChanged,
                onConfirmPutForSaleDialogVisibilityChanged = onConfirmPutForSaleDialogVisibilityChanged
            )
        }
    }
}


@Composable
private fun TokenDetailBody(
    scrollState: ScrollState,
    uiState: TokenDetailUiState,
    onBurnTokenCalled: (tokenId: BigInteger) -> Unit,
    onWithDrawFromSaleCalled: (tokenId: BigInteger) -> Unit,
    onPutItemForSaleCalled: (tokenId: BigInteger) -> Unit,
    onItemPriceChanged: (price: String) -> Unit,
    onConfirmBurnTokenDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmWithDrawFromSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit,
    onConfirmPutForSaleDialogVisibilityChanged: (isVisible: Boolean) -> Unit
) {
    with(uiState) {
        artCollectible?.let {
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Spacer(Modifier.height(HEADER_HEIGHT))
                if (isTokenOwner) {
                    CommonButton(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .width(300.dp),
                        text = if (isTokenAddedForSale) {
                            R.string.token_detail_with_draw_from_sale_button_text
                        } else {
                            R.string.token_detail_put_item_for_sale_button_text
                        },
                        onClick = {
                            if (isTokenAddedForSale) {
                                onConfirmWithDrawFromSaleDialogVisibilityChanged(true)
                            } else {
                                onConfirmPutForSaleDialogVisibilityChanged(true)
                            }
                        }
                    )
                    CommonButton(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .width(300.dp),
                        text = R.string.token_detail_burn_token_button_text,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        onClick = {
                            onConfirmBurnTokenDialogVisibilityChanged(true)
                        }
                    )
                }
            }
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
    onItemPriceChanged: (price: String) -> Unit,
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
                value = tokenPrice,
                numberOfDecimals = PRICE_NUMBER_OF_DECIMALS,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.matic_icon),
                        contentDescription = "Matic Icon",
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                },
                onValueChanged = {
                    onItemPriceChanged(it)
                }
            )
        }
    }
}
