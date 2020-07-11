package com.example.searchapp.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecycleViewEndlessScroll(layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    private var visibleThreshold = 1
    private lateinit var onLoadMoreListener : OnLoadMoreListener
    private var isLoading = false
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var mLayoutManager: RecyclerView.LayoutManager = layoutManager

    fun setLoaded(){
        isLoading = false
    }

    fun setOnLoadMoreListener(mSetOnLoadMoreListener: OnLoadMoreListener){
        this.onLoadMoreListener = mSetOnLoadMoreListener
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        totalItemCount = mLayoutManager.itemCount
        lastVisibleItem = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold){
            onLoadMoreListener.onLoadMore()
            isLoading = true
        }
    }
}