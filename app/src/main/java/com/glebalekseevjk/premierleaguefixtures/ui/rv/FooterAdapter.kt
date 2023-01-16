package com.glebalekseevjk.premierleaguefixtures.ui.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.databinding.ProgressRvListBinding
import com.glebalekseevjk.premierleaguefixtures.databinding.RetryRvListBinding

class FooterAdapter(private val retry: () -> Unit, private val displayCache: () -> Unit) :
    LoadStateAdapter<FooterAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ItemViewHolder, loadState: LoadState) {
        when (val binding = holder.binding) {
            is ProgressRvListBinding -> {
            }
            is RetryRvListBinding -> {
                binding.retryBtn.setOnClickListener { retry.invoke() }
                binding.cacheBtn.setOnClickListener {
                    displayCache.invoke()
                    retry.invoke()
                }
            }
        }
    }

    override fun getStateViewType(loadState: LoadState): Int = when (loadState) {
        is LoadState.Loading -> R.layout.progress_rv_list
        is LoadState.Error -> R.layout.retry_rv_list
        is LoadState.NotLoading -> R.layout.retry_rv_list
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemViewHolder {
        val layout = getStateViewType(loadState)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ItemViewHolder(binding)
    }
}