package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName

data class Question (
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("is_answered") val isAnswered: Boolean,
    @SerializedName("view_count") val viewCount: Int,
    @SerializedName("answer_count") val answerCount: Int,
    @SerializedName("score") val score: Int,
    @SerializedName("last_activity_date") val lastActivityDate: Int,
    @SerializedName("creation_date") val creationDate: Int,
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("link") val link: String,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("last_edit_date") val lastEditDate : Int,
    @SerializedName("accepted_answer_id") val acceptedAnswerId: Int
)