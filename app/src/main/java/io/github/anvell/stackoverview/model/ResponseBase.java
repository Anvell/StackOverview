package io.github.anvell.stackoverview.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;

public class ResponseBase<T> {

    @SerializedName("items")
    public ArrayList<T> items = new ArrayList<>();

    public ResponseBase() {
    }

    public ResponseBase(T[] items) {
        this.items.addAll(Arrays.asList(items));
    }
}