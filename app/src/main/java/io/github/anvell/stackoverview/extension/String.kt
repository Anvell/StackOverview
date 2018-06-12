package io.github.anvell.stackoverview.extension

import android.os.Build
import android.text.Html
import android.text.Spanned

fun String.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}