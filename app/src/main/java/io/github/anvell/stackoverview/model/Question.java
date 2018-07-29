package io.github.anvell.stackoverview.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

    @PrimaryKey
    @SerializedName("question_id")
    public int questionId;

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

    @SerializedName("title")
    public String title = "";
}