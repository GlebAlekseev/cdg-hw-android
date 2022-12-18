package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.glebalekseevjk.premierleaguefixtures.data.repository.MatchInfoRepositoryImpl
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class ListMatchesViewModel: ViewModel() {
    private val matchInfoRepository = MatchInfoRepositoryImpl()
    private val matchInfoUseCase = MatchInfoUseCase(matchInfoRepository)

    fun observeMatchList() = matchInfoUseCase.getMatchList()
    val currentRecyclerLayoutManager = MutableStateFlow(LayoutManagerViewType.VIEW_TYPE_LIST)

    companion object {
        enum class LayoutManagerViewType{
            VIEW_TYPE_GRID,
            VIEW_TYPE_LIST
        }
    }
}