package com.dreamsoftware.artcollectibles.ui.screens.mytokens

import android.content.Context
import com.dreamsoftware.artcollectibles.R
import com.dreamsoftware.artcollectibles.data.api.exception.GetTokensCreatedDataException
import com.dreamsoftware.artcollectibles.data.api.exception.GetTokensOwnedDataException
import com.dreamsoftware.artcollectibles.utils.IErrorMapper

class MyTokensScreenErrorMapper(
    private val context: Context
): IErrorMapper {
    override fun mapToMessage(ex: Throwable): String = context.getString(when(ex) {
        is GetTokensCreatedDataException -> R.string.my_tokens_tab_tokens_created_error
        is GetTokensOwnedDataException -> R.string.my_tokens_tab_tokens_owned_error
        else -> R.string.generic_error_exception
    })
}