package io.github.anvell.stackoverview.model;

import com.google.gson.annotations.SerializedName;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Question {

    @SerializedName("tags")
    public ArrayList<String> tags = new ArrayList<>();

    @SerializedName("is_answered")
    public Boolean isAnswered;

    @SerializedName("view_count")
    public int viewCount;

    @SerializedName("answer_count")
    public int answerCount;

    @SerializedName("score")
    public int score;

    @SerializedName("creation_date")
    public int creationDate;

    @SerializedName("question_id")
    public int questionId;

    @SerializedName("title")
    public String title = "";
}