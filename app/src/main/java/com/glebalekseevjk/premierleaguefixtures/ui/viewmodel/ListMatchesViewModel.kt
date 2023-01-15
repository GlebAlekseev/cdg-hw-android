package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ErrorType
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ResultType
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.ListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.LayoutManagerState
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.PaginationListMatchesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListMatchesViewModel @Inject constructor(
    private val matchInfoUseCase: MatchInfoUseCase
) : ViewModel() {
    private var isFirstError = true

    private val _paginationListMatchesState =
        MutableStateFlow<PaginationListMatchesState>(PaginationListMatchesState.NeedLoading())
    val paginationListMatchesState: StateFlow<PaginationListMatchesState>
        get() = _paginationListMatchesState

    private val _layoutManagerState =
        MutableStateFlow<LayoutManagerState>(LayoutManagerState.ViewTypeList)
    val layoutManagerState: StateFlow<LayoutManagerState>
        get() = _layoutManagerState

    private var loadNextPageJob: Job? = null
    private var loadNextPageFromLocalForRequestJob: Job? = null

    val userIntent = Channel<ListMatchesIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }

    private fun handleIntent(){
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect{
                when(it){
                    is ListMatchesIntent.LoadNextPage -> loadNextPage(it.onErrorCallBack)
                    is ListMatchesIntent.LoadNextPageFromLocalForRequest -> loadNextPageFromLocalForRequest()
                    is ListMatchesIntent.ResetPaginationListHolder -> resetPaginationListHolder(it.teamName, it.onResetCallBack)
                    is ListMatchesIntent.ToggleLayoutManagerState -> toggleLayoutManagerState(it.callback)
                    is ListMatchesIntent.StopLoading -> stopLoading()
                }
            }
        }
    }

    fun stopLoading(){
        _paginationListMatchesState.value = PaginationListMatchesState.Start()
    }

    fun toggleLayoutManagerState(callback: (drawableId: Int) -> Unit) {
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

    fun loadNextPage(onErrorCallBack: (ErrorType) -> Unit) {
        if (paginationListMatchesState.value.isFinish) return
        _paginationListMatchesState.value =
            PaginationListMatchesState.Loading(paginationListMatchesState.value)

        loadNextPageJob = viewModelScope.launch {
            // Получить диапазон элементов и добавить в paginationMatchInfoListHolder, если ошибка => вывести тост, если конец, то обновляю состояние.
            matchInfoUseCase.getMatchListRangeForPage(paginationListMatchesState.value.getPage())
                .collect {
                    when (it) {
                        is ResultType.Failure -> {
                            setPaginationListState(it.data ?: emptyList())

                            if (isFirstError) {
                                onErrorCallBack(it.errorType)
                                isFirstError = false
                            }
                        }
                        is ResultType.Loading -> {}
                        is ResultType.Success -> {
                            setPaginationListState(it.data)
                        }
                    }
                }
        }
    }

    private fun setPaginationListState(result: List<MatchInfo>){
        val isLast = result.size < MatchInfoRepository.TOTAL_PER_PAGE
        if (isLast) {
            val list =
                paginationListMatchesState.value.getPaginationMatchList() + result
            if (list.isEmpty()) {
                _paginationListMatchesState.value =
                    PaginationListMatchesState.Empty(paginationListMatchesState.value)
            } else {
                _paginationListMatchesState.value =
                    PaginationListMatchesState.Finish(
                        paginationListMatchesState.value,
                        result
                    )
            }
        } else {
            _paginationListMatchesState.value =
                PaginationListMatchesState.NeedLoading(
                    paginationListMatchesState.value,
                    result
                )
        }
    }


    fun loadNextPageFromLocalForRequest() {
        if (paginationListMatchesState.value.isFinish) return
        _paginationListMatchesState.value =
            PaginationListMatchesState.Loading(paginationListMatchesState.value)

        loadNextPageFromLocalForRequestJob = viewModelScope.launch {
            val result = matchInfoUseCase.searchTeamNamePagedMatchInfoList(
                paginationListMatchesState.value.getTeamName(),
                paginationListMatchesState.value.getPage()
            )
            when (result) {
                is ResultType.Failure -> {
                    setPaginationListState(result.data ?: emptyList())
                }
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    setPaginationListState(result.data)
                }
            }
        }
    }

    fun resetPaginationListHolder(teamName: String? = null, onResetCallBack: () -> Unit) {
        viewModelScope.launch {
            loadNextPageJob?.cancel()
            loadNextPageFromLocalForRequestJob?.cancel()
            _paginationListMatchesState.value =
                PaginationListMatchesState.NeedLoading(PaginationListMatchesState.Start(teamName))
            isFirstError = true
            onResetCallBack.invoke()
        }
    }
}