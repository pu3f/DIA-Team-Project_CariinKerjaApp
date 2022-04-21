package com.example.diateamproject.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {
    private var visibleThreshold = 2
    private var currentPage = 0
    private var previousPage = -1
    private var previousTotalItemCount = 0
    private var loading = true
    private var startingPageIndex = 0
    private var itemLoaded = 0
    private var mLayoutManager: RecyclerView.LayoutManager = layoutManager

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for ((i, value) in lastVisibleItemPositions.withIndex()) {
            if (i == 0) {
                maxSize = value
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = value
            }
        }
        return maxSize
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        var totalItemCount = mLayoutManager.itemCount
        itemLoaded = totalItemCount

        lastVisibleItemPosition =
            (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.loading = true
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false
            previousTotalItemCount = totalItemCount
            itemLoaded += totalItemCount
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > itemLoaded && totalItemCount >= 10) {
            currentPage++
            onLoadMore(currentPage, lastVisibleItemPosition + visibleThreshold, recyclerView)
            loading = true
        }
    }

    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView)
}