package com.example.toolsfordriver.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val str: String) : UiText()
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : UiText()

    @Composable
    fun asString(): String = when(this) {
        is DynamicString -> str
        is StringResource -> stringResource(id = resId, *args)
    }

    fun asString(context: Context): String = when(this) {
        is DynamicString -> str
        is StringResource -> context.getString(resId, *args)
    }
}