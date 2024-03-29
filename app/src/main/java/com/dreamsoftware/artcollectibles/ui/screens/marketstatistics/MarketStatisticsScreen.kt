package com.dreamsoftware.artcollectibles.ui.screens.marketstatistics

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.ErrorStateNotificationComponent
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.dreamsoftware.artcollectibles.ui.components.core.*
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModel

@Composable
fun MarketStatisticsScreen(
    viewModel: MarketStatisticsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceUiState(
        initialState = MarketStatisticsUiState(),
        lifecycle = lifecycle,
        viewModel = viewModel
    )
    val snackBarHostState = remember { SnackbarHostState() }
    with(viewModel) {
        LaunchedEffect(key1 = lifecycle, key2 = viewModel) {
            load()
        }
        MarketStatisticsComponent(
            snackBarHostState = snackBarHostState,
            state = uiState,
            onRefreshData = ::load,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
internal fun MarketStatisticsComponent(
    snackBarHostState: SnackbarHostState,
    state: MarketStatisticsUiState,
    onRefreshData: () -> Unit,
    onBackPressed: () -> Unit,
) {
    with(state) {
        BasicScreen(
            titleRes = R.string.market_statistics_main_title,
            snackBarHostState = snackBarHostState,
            enableVerticalScroll = true,
            centerTitle = true,
            navigationAction = TopBarAction(
                iconRes = R.drawable.back_icon,
                onActionClicked = onBackPressed
            ),
            menuActions = if (!isLoading && hasData()) {
                listOf(TopBarAction(iconRes = R.drawable.refresh_icon, onActionClicked = onRefreshData))
            } else {
                emptyList()
            },
            backgroundContent = {
                ErrorStateNotificationComponent(
                    modifier = Modifier.align(Alignment.Center),
                    isVisible = !isLoading && !hasData(),
                    imageRes = R.drawable.not_data_found,
                    title = stringResource(id = R.string.market_statistics_not_found_message),
                    isRetryButtonVisible = true,
                    onRetryCalled = onRefreshData
                )
            },
            screenContent = {
                LoadingDialog(isShowingDialog = isLoading)
                mostPurchasesChartEntryModel?.let {
                    MarketStatisticChart(
                        titleRes = R.string.market_statistics_users_with_more_purchases_title,
                        chartEntryModel = it
                    )
                }
                mostSoldTokensChartEntryModel?.let {
                    MarketStatisticChart(
                        titleRes = R.string.market_statistics_most_sold_tokens_title,
                        chartEntryModel = it
                    )
                }
                mostSalesChartEntryModel?.let {
                    MarketStatisticChart(
                        titleRes = R.string.market_statistics_users_with_more_sales_title,
                        chartEntryModel = it
                    )
                }
                mostTokensCreatedChartEntryModel?.let {
                    MarketStatisticChart(
                        titleRes = R.string.market_statistics_users_with_more_tokens_created_title,
                        chartEntryModel = it
                    )
                }
                mostCancelledTokensChartEntryModel?.let {
                    MarketStatisticChart(
                        titleRes = R.string.market_statistics_most_cancelled_tokens_title,
                        chartEntryModel = it
                    )
                }

            }
        )
    }
}

@Composable
private fun MarketStatisticChart(
    @StringRes titleRes: Int,
    chartEntryModel: ChartEntryModel
) {
    StatisticChart(
        type = ChartTypeEnum.COLUMN,
        titleRes = titleRes,
        entryModel = chartEntryModel,
        bottomAxisValueFormatter = { value, chartValues ->
            (chartValues.chartEntryModel.entries.first()
                .getOrNull(value.toInt()) as? ChartUiTextEntry)
                ?.label
                .orEmpty()
        }
    )
}

@Composable
private fun StatisticChart(
    type: ChartTypeEnum,
    @StringRes titleRes: Int,
    entryModel: ChartEntryModel,
    bottomAxisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>? = null
) {
    if(entryModel.entries.firstOrNull()?.isNotEmpty() == true) {
        Card(
            modifier = Modifier.padding(20.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(Color.White.copy(alpha = 0.7f)),
            shape = RoundedCornerShape(27.dp),
            border = BorderStroke(3.dp, Color.White)
        ) {
            CommonChart(
                modifier = Modifier.padding(8.dp),
                titleRes = titleRes,
                entryModel = entryModel,
                type = type,
                bottomAxisValueFormatter = bottomAxisValueFormatter
            )
        }
    }
}

