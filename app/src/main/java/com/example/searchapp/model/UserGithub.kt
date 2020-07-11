package com.example.searchapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserGithub(
    @SerializedName("id")
    val id : Int = 0,
    @SerializedName("login")
    val username : String = "",
    @SerializedName("avatar_url")
    val img : String = "",
    @SerializedName("html_url")
    val link : String = ""
) : Serializable