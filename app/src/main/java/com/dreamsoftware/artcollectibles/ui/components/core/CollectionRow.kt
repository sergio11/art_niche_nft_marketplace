package com.dreamsoftware.artcollectibles.ui.components.core

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.google.common.collect.Iterables

@Composable
fun <T: Any>CollectionRow(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    reverseStyle: Boolean = false,
    items: Iterable<T>,
    onBuildItem: @Composable LazyItemScope.(modifier: Modifier, item: T) -> Unit,
    onShowAllItems: (() -> Unit)? = null,
    onItemSelected: (item: T) -> Unit = {}
) {
    if (!Iterables.isEmpty(items)) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CommonText(
                    modifier = Modifier.padding(8.dp),
                    type = CommonTextTypeEnum.TITLE_LARGE,
                    titleText = stringResource(id = titleRes),
                    textColor = if (reverseStyle) {
                        Color.Black
                    } else {
                        Color.White
                    },
                    maxLines = 2
                )
                onShowAllItems?.let {
                    Image(
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(35.dp)
                            .clickable { it() },
                        painter = painterResource(R.drawable.arrow_right_icon),
                        contentDescription = "onShowAllItems",
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(if (reverseStyle) {
                            Color.Black
                        } else {
                            Color.White
                        })
                    )
                }
            }
            LazyRow(
                modifier = Modifier.padding(vertical = 10.dp),
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(Iterables.size(items)) { idx ->
                    with(Iterables.get(items, idx)) {
                        onBuildItem(
                            modifier = Modifier.clickable {
                                onItemSelected(this)
                            },
                            item = this
                        )
                    }
                }
            }
        }
    }
}