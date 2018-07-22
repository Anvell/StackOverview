package io.github.anvell.stackoverview.model;

import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("profile_image")
    public String profileImage = "";

    @SerializedName("display_name")
    public String displayName = "";

    @SerializedName("link")
    public String link = "";
}