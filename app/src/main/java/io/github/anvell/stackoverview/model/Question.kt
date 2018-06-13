package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName
import java.net.URLDecoder

data class Question (
    @SerializedName("tags") val tags: List<String> = listOf(),
    @SerializedName("is_answered") val isAnswered: Boolean = false,
    @SerializedName("view_count") val viewCount: Int = 0,
    @SerializedName("answer_count") val answerCount: Int = 0,
    @SerializedName("score") val score: Int = 0,
    @SerializedName("creation_date") val creationDate: Int = 0,
    @SerializedName("question_id") val questionId: Int = 0,
    @SerializedName("title") val title: String = ""
)