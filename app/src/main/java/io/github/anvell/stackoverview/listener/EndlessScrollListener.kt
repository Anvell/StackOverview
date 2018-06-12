package io.github.anvell.stackoverview.listener

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class EndlessScrollListener(
        private val onLoadMore: () -> Unit,
        val layoutManager: LinearLayoutManager)
    : RecyclerView.OnScrollListener() {

    private var visibleThreshold = 5
    private var previousTotalItemCount = 0
    private var loading = true

    override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {

        if (dy <= 0) return

        var lastVisibleItemPosition = 0
        val totalItemCount = layoutManager.itemCount
        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold >= totalItemCount) {
            onLoadMore()
            loading = true
        }
    }
}
