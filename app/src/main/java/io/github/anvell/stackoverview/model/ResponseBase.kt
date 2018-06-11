package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName

open class ResponseBase<T>(
    @SerializedName("items") val items: List<T>
)