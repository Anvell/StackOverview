package io.github.anvell.stackoverview.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionDetails extends Question {

    @SerializedName("answers")
    public ArrayList<Answer> answers = new ArrayList<>();

    @SerializedName("owner")
    public Owner owner = new Owner();

    @SerializedName("link")
    public String link = "";

    @SerializedName("body")
    public String body = "";

    @Expose(serialize = false, deserialize = false)
    public boolean isFavorite;

    public void updateAnswerCount() {
        answerCount = answers.size();
    }
}