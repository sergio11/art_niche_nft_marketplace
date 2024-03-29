package com.dreamsoftware.artcollectibles.ui.components

import android.view.KeyEvent.KEYCODE_ENTER
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.domain.models.Comment
import com.dreamsoftware.artcollectibles.domain.models.UserInfo
import com.dreamsoftware.artcollectibles.ui.components.core.CommonButton
import com.dreamsoftware.artcollectibles.ui.components.core.CommonText
import com.dreamsoftware.artcollectibles.ui.components.core.CommonTextTypeEnum
import com.dreamsoftware.artcollectibles.ui.theme.DarkPurple
import com.dreamsoftware.artcollectibles.ui.theme.Purple40
import com.google.common.collect.Iterables

@Composable
fun PublishCommentComponent(
    modifier: Modifier = Modifier,
    authUserInfo: UserInfo? = null,
    commentsCount: Long? = null,
    enabled: Boolean = true,
    lastComments: Iterable<Comment> = emptyList(),
    onPublishComment: (comment: String) -> Unit = {},
    onSeeCommentDetail: (comment: Comment) -> Unit = {},
    onSeeAllComments: (() -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var placeholderEnabled by remember { mutableStateOf(true) }
    var comment by remember { mutableStateOf("") }
    val publishCommentCaller = {
        if (comment.isNotBlank()) {
            onPublishComment(comment).also {
                focusManager.clearFocus()
                comment = ""
            }
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
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
                titleText = stringResource(id = R.string.token_detail_comments_publish_title_text),
                singleLine = true
            )
            onSeeAllComments?.let {
                Image(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(35.dp)
                        .clickable { it() },
                    painter = painterResource(R.drawable.arrow_right_icon),
                    contentDescription = "onSeeAllComments",
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(DarkPurple)
                )
            }
        }
        repeat(Iterables.size(lastComments)) {
            with(Iterables.get(lastComments, it)) {
                CommonText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .padding(horizontal = 8.dp)
                        .clickable { onSeeCommentDetail(this) },
                    titleText = "${user.name}: $text",
                    type = CommonTextTypeEnum.BODY_MEDIUM,
                    singleLine = true
                )
            }
        }
        commentsCount?.let {
            if (it > 0) {
                CommonText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            onSeeAllComments?.invoke()
                        },
                    type = CommonTextTypeEnum.TITLE_SMALL,
                    titleText = stringResource(id = R.string.token_detail_comments_see_more_text, it),
                    textColor = Color.LightGray,
                    singleLine = true
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAccountProfilePicture(size = 50.dp, userInfo = authUserInfo)
            BasicTextField(
                modifier = Modifier
                    .weight(1f, true)
                    .padding(horizontal = 8.dp)
                    .onFocusChanged {
                        placeholderEnabled = !it.isFocused
                    }
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KEYCODE_ENTER) {
                            publishCommentCaller()
                            true
                        } else {
                            false
                        }
                    },
                value = comment.ifBlank {
                    if (placeholderEnabled) {
                        stringResource(id = R.string.token_detail_comments_publish_placeholder_text)
                    } else {
                        ""
                    }
                },
                onValueChange = {
                    comment = it
                },
                enabled = enabled,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        publishCommentCaller()
                    }
                ),
                decorationBox = { innerTextField ->
                    Row(
                        Modifier
                            .background(
                                Color.LightGray.copy(alpha = 0.6f),
                                RoundedCornerShape(percent = 30)
                            )
                            .padding(16.dp)
                            .focusRequester(focusRequester)
                    ) {
                        innerTextField()
                    }
                }
            )
            CommonButton(
                modifier = Modifier.padding(4.dp),
                text = R.string.token_detail_comments_publish_button_text,
                widthDp = 120.dp,
                enableBorder = false,
                enabled = enabled,
                containerColor = Color.Transparent,
                contentColor = Purple40,
                textType = CommonTextTypeEnum.TITLE_SMALL,
                buttonShape = ButtonDefaults.textShape,
                onClick = publishCommentCaller
            )
        }
    }
}