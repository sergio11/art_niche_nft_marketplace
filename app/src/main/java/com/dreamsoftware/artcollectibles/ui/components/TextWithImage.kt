package com.dreamsoftware.artcollectibles.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum

private val DEFAULT_ICON_SIZE = 20.dp

@Composable
fun TextWithImage(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int,
    tintColor: Color = Color.Black,
    text: String,
    iconSize: Dp = DEFAULT_ICON_SIZE
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(iconSize),
            colorFilter = ColorFilter.tint(tintColor)
        )
        CommonText(
            modifier = Modifier.padding(start = 5.dp),
            type = CommonTextTypeEnum.TITLE_MEDIUM,
            textColor = tintColor,
            titleText = text
        )
    }
}