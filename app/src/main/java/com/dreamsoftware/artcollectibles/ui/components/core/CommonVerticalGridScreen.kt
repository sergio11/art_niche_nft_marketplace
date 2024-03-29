package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.ui.components.ErrorStateNotificationComponent
import com.dreamsoftware.artcollectibles.ui.components.LoadingDialog
import com.google.common.collect.Iterables

@Composable
fun <T: Any>CommonVerticalGridScreen(
    lazyGridState: LazyGridState,
    snackBarHostState: SnackbarHostState,
    isLoading: Boolean,
    items: Iterable<T>,
    appBarTitle: String,
    @StringRes noDataFoundMessageId: Int,
    onRetryCalled: () -> Unit,
    onBackPressed: () -> Unit,
    onBuildContent: @Composable LazyGridItemScope.(item: T) -> Unit
) {
    LoadingDialog(isShowingDialog = isLoading)
    BasicScreen(
        snackBarHostState = snackBarHostState,
        titleText = appBarTitle,
        centerTitle = true,
        navigationAction = TopBarAction(
            iconRes = R.drawable.back_icon,
            onActionClicked = onBackPressed
        ),
        backgroundContent = {
            ErrorStateNotificationComponent(
                modifier = Modifier.align(Alignment.Center),
                isVisible = !isLoading && Iterables.isEmpty(items),
                imageRes = R.drawable.not_data_found,
                title = stringResource(id = noDataFoundMessageId),
                isRetryButtonVisible = true,
                onRetryCalled = onRetryCalled
            )
        },
        screenContent = {
            CommonLazyVerticalGrid(
                state = lazyGridState,
                items = items,
                onBuildContent = onBuildContent
            )
        }
    )
}