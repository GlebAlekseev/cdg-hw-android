package com.glebalekseevjk.premierleaguefixtures.ui.fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class PaginationScrollListener(private val layoutManager: GridLayoutManager): RecyclerView.OnScrollListener() {
    private var lock = false
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!lock && isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                CoroutineScope(Dispatchers.Main).launch {
                    lock = true
                    loadMoreItems{
                        lock = false
                    }
                }
            }
        }
    }

    protected abstract suspend fun loadMoreItems(onFinishCallback: ()->Unit)

    protected abstract fun isLastPage(): Boolean

    protected abstract fun isLoading(): Boolean
}