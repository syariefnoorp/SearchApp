package com.example.searchapp.ui

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchapp.R
import com.example.searchapp.adapter.OnLoadMoreListener
import com.example.searchapp.adapter.RecycleViewEndlessScroll
import com.example.searchapp.adapter.UserGithubAdapter
import com.example.searchapp.model.UserGithub
import io.isfaaghyth.rak.Rak
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener{

    private lateinit var viewModel : UserViewModel
    private lateinit var itemCells : MutableList<UserGithub?>
    private lateinit var tempItemCells : MutableList<UserGithub?>
    private lateinit var scrollListener : RecycleViewEndlessScroll
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: UserGithubAdapter
    private var queryInput : String = ""
    private var page = 1
    private var pageUpdate = 1
    private var totalPage = 1
    private var markNotConnectInternet : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchView.setOnQueryTextListener(this)

        itemCells = mutableListOf()
        tempItemCells = mutableListOf()
        tempItemCells.add(UserGithub(0,"temp","temp","link"))
        setComponentRV()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!=null){
            if (query != queryInput || (query == queryInput && markNotConnectInternet)){
                itemCells.clear()
                tempItemCells.clear()
                totalPage = 1

                setComponentRV()

                page = 1
                pageUpdate = 1
            }

            queryInput = query
            adapter.addLoadingView()
            requestData(query)
        }else{
            Toast.makeText(this, "Text Search Tidak Boleh Kosong, Silahkan Mengisi Text Search",
                Toast.LENGTH_LONG).show()
        }

        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    private fun requestData(query : String?){
        if (page > totalPage){
            return
        }

        if (isConnectInternet()){
            markNotConnectInternet = false
            viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
            viewModel.getData(query,page)
            viewModel.setData().observe(this, Observer { task ->

                if (task!=null){
                    itemCells = task
                }else{
                    val status = Rak.grab<String>("status_result")
                    if (status == "not found"){
                        errorView.text = "Pengguna Tidak Ditemukan"
                        adapter.removeLoadingView()
                    }else if (status == "failure"){
                        errorView.text = "Gagal Mendapatkan Data, Silahkan Mencoba Lagi"
                        adapter.removeLoadingView()
                    }
                    layoutFailed.visibility = View.VISIBLE
                }

                if (itemCells != tempItemCells) {
                    adapter.removeLoadingView()
                    adapter.addData(itemCells)
                    tempItemCells = itemCells
                    scrollListener.setLoaded()

                    rvHasilSearch.post {
                        adapter.notifyDataSetChanged()
                    }
                }
            })

            ++page
            setEndlessScrollListener()

        }else{
            markNotConnectInternet = true
            errorView.text = "Tidak Terhubung Dengan Internet"
            layoutFailed.visibility = View.VISIBLE
            rvHasilSearch.visibility = View.GONE
        }
    }

    private fun setComponentRV(){
        layoutFailed.visibility = View.GONE
        rvHasilSearch.visibility = View.VISIBLE

        mLayoutManager = LinearLayoutManager(this)
        rvHasilSearch.layoutManager = mLayoutManager
        rvHasilSearch.setHasFixedSize(true)

        adapter = UserGithubAdapter(this,itemCells)
        adapter.notifyDataSetChanged()
        rvHasilSearch.adapter = adapter
    }

    private fun setEndlessScrollListener(){
        mLayoutManager = LinearLayoutManager(this)
        scrollListener = RecycleViewEndlessScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
        OnLoadMoreListener {
            override fun onLoadMore() {
                totalPage = Rak.grab("total_page",1)

                if (totalPage >= 20){
                    totalPage = 20
                }

                if (page <= totalPage && page > pageUpdate){
                    ++pageUpdate
                    loadMoreData()
                }else{
                    Log.i("info","reach end of data")
                }
            }
        })
        rvHasilSearch.addOnScrollListener(scrollListener)
    }

    private fun loadMoreData(){
        adapter.addLoadingView()

        Handler().postDelayed({
            requestData(queryInput)
        }, 10000)
    }

    private fun isConnectInternet() : Boolean{
        //check internet
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork?.isConnectedOrConnecting == true
    }
}