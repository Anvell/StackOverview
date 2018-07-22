package io.github.anvell.stackoverview.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private final int visibleThreshold = 5;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private LinearLayoutManager layoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void onScrolled(RecyclerView view, int dx, int dy) {

        if (dy <= 0) return;

        int lastVisibleItemPosition;
        int totalItemCount = layoutManager.getItemCount();
        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                loading = true;
            }
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && lastVisibleItemPosition + visibleThreshold >= totalItemCount) {
            onLoadMore(totalItemCount, view);
            loading = true;
        }
    }

    protected abstract void onLoadMore(int totalItemsCount, RecyclerView view);
}
