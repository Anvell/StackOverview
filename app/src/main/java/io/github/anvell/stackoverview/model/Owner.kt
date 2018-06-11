package io.github.anvell.stackoverview.model

import com.google.gson.annotations.SerializedName

data class Owner (
    @SerializedName("profile_image") val profileImage: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("link") val link: String
)