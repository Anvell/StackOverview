package io.github.anvell.stackoverview.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBase<T> {

    @SerializedName("items")
    public ArrayList<T> items = new ArrayList<>();
}