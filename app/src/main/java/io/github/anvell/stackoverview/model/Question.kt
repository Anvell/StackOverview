package io.github.anvell.stackoverview.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Question {

    @SerializedName("tags")
    @Expose
    var tags: List<String>? = null

    @SerializedName("is_answered")
    @Expose
    var isAnswered: Boolean? = null

    @SerializedName("view_count")
    @Expose
    var viewCount: Int? = null

    @SerializedName("answer_count")
    @Expose
    var answerCount: Int? = null

    @SerializedName("score")
    @Expose
    var score: Int? = null

    @SerializedName("last_activity_date")
    @Expose
    var lastActivityDate: Int? = null

    @SerializedName("creation_date")
    @Expose
    var creationDate: Int? = null

    @SerializedName("question_id")
    @Expose
    var questionId: Int? = null

    @SerializedName("link")
    @Expose
    var link: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    @SerializedName("last_edit_date")
    @Expose
    var lastEditDate: Int? = null

    @SerializedName("accepted_answer_id")
    @Expose
    var acceptedAnswerId: Int? = null

}