package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName

data class QuestionDetails (
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("answers") val answers: MutableList<Answer>,
    @SerializedName("owner") val owner: Owner,
    @SerializedName("view_count") val viewCount: Int,
    @SerializedName("score") val score: Int,
    @SerializedName("creation_date") val creationDate: Int,
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("link") val link: String,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)