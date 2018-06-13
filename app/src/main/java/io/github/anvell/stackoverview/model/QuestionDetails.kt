package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName

data class QuestionDetails(
    @SerializedName("tags") val tags: List<String>? = listOf(),
    @SerializedName("answers") val answers: MutableList<Answer>? = mutableListOf(),
    @SerializedName("owner") val owner: Owner = Owner(),
    @SerializedName("view_count") val viewCount: Int = 0,
    @SerializedName("score") val score: Int = 0,
    @SerializedName("creation_date") val creationDate: Int = 0,
    @SerializedName("question_id") val questionId: Int = 0,
    @SerializedName("link") val link: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("body") val body: String = ""
)