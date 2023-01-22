package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import com.glebalekseevjk.premierleaguefixtures.ui.paging.SearchListMatchesDataSource
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.SearchListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.LayoutManagerState
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.SearchListMatchesState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchListMatchesViewModel @Inject constructor(
    private val matchInfoUseCase: MatchInfoUseCase
) : ViewModel() {
    val layoutManagerState: StateFlow<LayoutManagerState>
        get() = _layoutManagerState
    val searchListMatchesState: StateFlow<SearchListMatchesState>
        get() = _searchListMatchesState
    val teamName: StateFlow<String>
        get() = _teamName
    val userIntent = Channel<SearchListMatchesIntent>(Channel.UNLIMITED)
    val pagingListMatches = Pager(
        PagingConfig(
            pageSize = MatchInfoRepository.TOTAL_PER_PAGE,
            prefetchDistance = 2,
        )
    ) {
        SearchListMatchesDataSource(matchInfoUseCase, teamName.value)
    }.flow.cachedIn(viewModelScope)

    private val _searchListMatchesState =
        MutableStateFlow<SearchListMatchesState>(SearchListMatchesState.Start)
    private val _teamName = MutableStateFlow("")
    private val _layoutManagerState =
        MutableStateFlow<LayoutManagerState>(LayoutManagerState.ViewTypeList)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is SearchListMatchesIntent.ToggleLayoutManagerState -> toggleLayoutManagerState(
                        it.callback
                    )
                    is SearchListMatchesIntent.SetTeamName -> setTeamName(it.teamName)
                    is SearchListMatchesIntent.SetIdleState -> setIdle()
                    is SearchListMatchesIntent.SetNotFoundState -> setNotFound()
                    is SearchListMatchesIntent.SetLoading -> setLoading()
                }
            }
        }
    }

    private fun toggleLayoutManagerState(callback: (drawableId: Int) -> Unit) {
        when (layoutManagerState.value) {
            LayoutManagerState.ViewTypeGrid -> {
                _layoutManagerState.value = LayoutManagerState.ViewTypeList
                callback(R.drawable.ic_columns_24)
            }
            LayoutManagerState.ViewTypeList -> {
                _layoutManagerState.value = LayoutManagerState.ViewTypeGrid
                callback(R.drawable.ic_rows_24)
            }
        }
    }

    private fun setTeamName(teamName: String) {
        _teamName.value = teamName
    }

    private fun setIdle() {
        _searchListMatchesState.value = SearchListMatchesState.Idle
    }

    private fun setNotFound() {
        _searchListMatchesState.value = SearchListMatchesState.NotFound
    }

    private fun setLoading() {
        _searchListMatchesState.value = SearchListMatchesState.Loading
    }
}