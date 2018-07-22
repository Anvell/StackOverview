package io.github.anvell.stackoverview.model;

import com.google.gson.annotations.SerializedName;

public class Answer {
    @SerializedName("owner")
    public Owner owner;

    @SerializedName("is_accepted")
    public Boolean isAccepted;

    @SerializedName("creation_date")
    public int creationDate;

    @SerializedName("answer_id")
    public int answerId;

    @SerializedName("link")
    public String link;

    @SerializedName("body")
    public String body;
}