package io.github.anvell.stackoverview.model

import android.content.ClipData.Item

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QuestionsResponce {

    @SerializedName("items")
    @Expose
    var items: List<Item>? = null

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