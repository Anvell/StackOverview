package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName

data class Answer (
        @SerializedName("owner") val owner: Owner,
        @SerializedName("is_accepted") val isAccepted: Boolean,
        @SerializedName("creation_date") val creationDate: Int,
        @SerializedName("answer_id") val answerId: Int,
        @SerializedName("link") val link: String,
        @SerializedName("body") val body: String
)