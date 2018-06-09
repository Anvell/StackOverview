package io.github.anvell.stackoverview.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QuestionsResponse {

    @SerializedName("items")
    @Expose
    var items: List<Question>? = null

    @SerializedName("has_more")
    @Expose
    var hasMore: Boolean? = null

    @SerializedName("quota_max")
    @Expose
    var quotaMax: Int? = null

    @SerializedName("quota_remaining")
    @Expose
    var quotaRemaining: Int? = null
}