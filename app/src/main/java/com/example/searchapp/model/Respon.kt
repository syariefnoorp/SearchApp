package com.example.searchapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Respon(
    @SerializedName("total_count")
    val tc : Int = 0,
    @SerializedName("incomplete_results")
    val ir : Boolean,
    @SerializedName("items")
    val item : MutableList<UserGithub?>
) : Serializable