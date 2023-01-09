package com.glebalekseevjk.premierleaguefixtures.ui.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.databinding.MatchItemRvGridBinding
import com.glebalekseevjk.premierleaguefixtures.databinding.MatchItemRvListBinding
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo

class MatchListAdapter :
    ListAdapter<MatchInfo, MatchListAdapter.MatchViewHolder>(MatchInfoDiffCallBack()) {
    var openMatchDetailClickListener: ((matchNumber: Int) -> Unit)? = null
    var viewType = VIEW_TYPE_LIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_GRID -> R.layout.match_item_rv_grid
            VIEW_TYPE_LIST -> R.layout.match_item_rv_list
            else -> throw RuntimeException("Unknown view type $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return MatchViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return if (viewType == VIEW_TYPE_LIST) VIEW_TYPE_LIST else VIEW_TYPE_GRID
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val binding = holder.binding
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            openMatchDetailClickListener?.invoke(item.matchNumber)
        }
        when (binding) {
            is MatchItemRvListBinding -> {
                binding.match = item
            }
            is MatchItemRvGridBinding -> {
                binding.match = item
            }
        }
    }

    inner class MatchViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val VIEW_TYPE_GRID = 2
        const val VIEW_TYPE_LIST = 1
    }
}

