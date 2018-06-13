package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName

class QuestionsResponse(
    items: List<Question> = listOf(),
    @SerializedName("has_more") val hasMore: Boolean = false,
    @SerializedName("quota_max") val quotaMax: Int = 0,
    @SerializedName("quota_remaining") val quotaRemaining: Int = 0
) : ResponseBase<Question>(items)