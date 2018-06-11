package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName
import java.net.URLDecoder

data class Question (
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("is_answered") val isAnswered: Boolean,
    @SerializedName("view_count") val viewCount: Int,
    @SerializedName("answer_count") val answerCount: Int,
    @SerializedName("score") val score: Int,
    @SerializedName("creation_date") val creationDate: Int,
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("title") val title: String
)