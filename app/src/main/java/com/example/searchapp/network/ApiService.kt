package com.example.searchapp.network

import com.example.searchapp.model.Respon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    fun getUserGithub(@Url url : String): Call<Respon>
}