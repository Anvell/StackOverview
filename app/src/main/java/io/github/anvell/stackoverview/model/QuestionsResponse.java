package io.github.anvell.stackoverview.model;

import com.google.gson.annotations.SerializedName;

public class QuestionsResponse extends ResponseBase<Question> {

    @SerializedName("has_more")
    public Boolean hasMore;

    @SerializedName("quota_max")
    public int quotaMax;

    @SerializedName("quota_remaining")
    public int quotaRemaining;
}