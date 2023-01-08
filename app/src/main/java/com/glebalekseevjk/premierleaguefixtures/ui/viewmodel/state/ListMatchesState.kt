package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo

data class ListMatchesState(
    val layoutManagerViewType: LayoutManagerViewType = LayoutManagerViewType.VIEW_TYPE_LIST,
    val listMatches: List<MatchInfo> = emptyList(),
    val isLastPage: Boolean = false,
    val isLoading: Boolean = true,
    val currentPage: Int = 0
){
    companion object{
        enum class LayoutManagerViewType{
            VIEW_TYPE_GRID,
            VIEW_TYPE_LIST
        }
    }
}