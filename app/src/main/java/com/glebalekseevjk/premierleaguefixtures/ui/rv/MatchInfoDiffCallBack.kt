package com.glebalekseevjk.premierleaguefixtures.ui.rv

import androidx.recyclerview.widget.DiffUtil
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo

class MatchInfoDiffCallBack : DiffUtil.ItemCallback<MatchInfo>() {
    override fun areItemsTheSame(oldItem: MatchInfo, newItem: MatchInfo): Boolean {
        return oldItem.matchNumber == newItem.matchNumber
    }

    override fun areContentsTheSame(oldItem: MatchInfo, newItem: MatchInfo): Boolean {
        return oldItem == newItem
    }
}