package com.example.diateamproject.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollingRecyclerView(layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val countItem = linearLayoutManager.itemCount
        val lastVisiblePosition =
            linearLayoutManager.findLastCompletelyVisibleItemPosition()
        val isLastPosition = countItem.minus(1) == lastVisiblePosition
        if (isLastPosition && countItem >= 10) {
            onLoadMore(countItem, recyclerView)
        }
    }

    abstract fun onLoadMore(totalItemsCount: Int, recyclerView: RecyclerView)
}