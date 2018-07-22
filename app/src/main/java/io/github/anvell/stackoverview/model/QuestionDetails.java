package io.github.anvell.stackoverview.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionDetails {

    @SerializedName("tags")
    public ArrayList<String> tags = new ArrayList<>();

    @SerializedName("answers")
    public ArrayList<Answer> answers = new ArrayList<>();

    @SerializedName("owner")
    public Owner owner = new Owner();

    @SerializedName("view_count")
    public int viewCount;

    @SerializedName("score")
    public int score;

    @SerializedName("creation_date")
    public int creationDate;

    @SerializedName("question_id")
    public int questionId;

    @SerializedName("link")
    public String link = "";

    @SerializedName("title")
    public String title = "";

    @SerializedName("body")
    public String body = "";
}