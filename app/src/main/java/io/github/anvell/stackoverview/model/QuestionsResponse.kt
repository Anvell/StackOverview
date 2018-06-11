package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName

class QuestionsResponse(
    items: List<Question>,
    @SerializedName("has_more") val hasMore: Boolean,
    @SerializedName("quota_max") val quotaMax: Int,
    @SerializedName("quota_remaining") val quotaRemaining: Int
) : ResponseBase<Question>(items)