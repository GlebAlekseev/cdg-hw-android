package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import com.glebalekseevjk.premierleaguefixtures.ui.paging.ListMatchesDataSource
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.ListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.LayoutManagerState
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.MatchDetailState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListMatchesViewModel @Inject constructor(
    private val matchInfoUseCase: MatchInfoUseCase
) : ViewModel() {
    val layoutManagerState: StateFlow<LayoutManagerState>
        get() = _layoutManagerState
    val listMatchesState: StateFlow<ListMatchesState>
        get() = _listMatchesState
    val userIntent = Channel<ListMatchesIntent>(Channel.UNLIMITED)
    val pagingListMatches = Pager(
        PagingConfig(
            pageSize = MatchInfoRepository.TOTAL_PER_PAGE,
            prefetchDistance = 2,
            enablePlaceholders = true,
            initialLoadSize = MatchInfoRepository.TOTAL_PER_PAGE,
            maxSize = MAXIMUM_LIMIT_OF_CACHE_ITEMS
        )
    ) {
        ListMatchesDataSource(matchInfoUseCase).also { dataSource = it }
    }.flow.cachedIn(viewModelScope)

    private val _layoutManagerState =
        MutableStateFlow<LayoutManagerState>(LayoutManagerState.ViewTypeList)
    private val _listMatchesState =
        MutableStateFlow<ListMatchesState>(ListMatchesState.Start)
    private lateinit var dataSource: ListMatchesDataSource

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is ListMatchesIntent.ToggleLayoutManagerState -> toggleLayoutManagerState(it.callback)
                    is ListMatchesIntent.EnableCacheMode -> enableCacheMode()
                    is ListMatchesIntent.SetIdleState -> setIdle()
                    is ListMatchesIntent.SetNotFoundState -> setNotFound()
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

    private fun enableCacheMode() {
        dataSource.isLocal = true
    }

    private fun setIdle() {
        _listMatchesState.value = ListMatchesState.Idle
    }

    private fun setNotFound() {
        _listMatchesState.value = ListMatchesState.NotFound
    }

    companion object {
        private const val MAXIMUM_LIMIT_OF_CACHE_ITEMS = 200
    }
}