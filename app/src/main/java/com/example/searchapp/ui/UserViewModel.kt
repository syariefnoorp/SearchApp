package com.example.searchapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.searchapp.model.Respon
import com.example.searchapp.model.UserGithub
import com.example.searchapp.network.NetworkConfig
import io.isfaaghyth.rak.Rak
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

class UserViewModel : ViewModel() {

    private var data = MutableLiveData<MutableList<UserGithub?>>()

    fun getData(query : String?,page : Int){
        val baseUrl = "https://api.github.com/search/users?q=$query&per_page=50&page=$page"
        NetworkConfig().api().getUserGithub(baseUrl).enqueue(object : Callback<Respon> {
            override fun onFailure(call: Call<Respon>, t: Throwable) {
                data.value = null
                Rak.entry("status_result","failure")
            }

            override fun onResponse(call: Call<Respon>, response: Response<Respon>) {
                if(response.isSuccessful){
                    val totalCount = (response.body()!!.tc).toDouble()
                    if (totalCount > 0){
                        val totalPage = ceil(totalCount/50.0)
                        Rak.entry("total_page",totalPage.toInt())
                        data.value = response.body()!!.item
                    }else{
                        Rak.entry("status_result","not found")
                        Rak.entry("total_page",0)
                        data.value = null
                    }
                }
            }
        })
    }

    fun setData() : MutableLiveData<MutableList<UserGithub?>> {
        return data
    }
}