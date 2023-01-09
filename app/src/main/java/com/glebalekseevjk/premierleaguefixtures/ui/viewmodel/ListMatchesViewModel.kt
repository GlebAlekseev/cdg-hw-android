package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ResultStatus
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListMatchesViewModel @Inject constructor(
    private val matchInfoUseCase: MatchInfoUseCase
) : BaseViewModel<ListMatchesState>(ListMatchesState()) {
    private var isFirstError = true

    private val _paginationMatchInfoListHolder: MutableStateFlow<List<MatchInfo>> =
        MutableStateFlow(
            emptyList()
        )

    init {
        subscribeOnDataSource(_paginationMatchInfoListHolder.asLiveData()) { response, state ->
            state.copy(
                listMatches = response
            )
        }
    }

    fun loadNextPage(onErrorCallBack: (String) -> Unit) {
        if (currentState.isLastPage) return
        val newPage = currentState.currentPage + 1

        updateState {
            it.copy(
                isLoading = true,
                isLoadingPage = true,
                currentPage = newPage
            )
        }

        viewModelScope.launch {
            // Получить диапазон элементов и добавить в paginationMatchInfoListHolder, если ошибка => вывести тост, если конец, то обновляю состояние.
            matchInfoUseCase.getMatchListRangeForPage(newPage).collect {
                when (it.status) {
                    ResultStatus.SUCCESS, ResultStatus.FAILURE -> {
                        val isLast = it.data.size < MatchInfoRepository.TOTAL_PER_PAGE
                        with(Dispatchers.Main) {
                            updateState {
                                it.copy(
                                    isLoading = !isLast,
                                    isLastPage = isLast
                                )
                            }
                        }

                        if (currentState.isLoadingPage) {
                            _paginationMatchInfoListHolder.emit(_paginationMatchInfoListHolder.value + it.data)
                            updateState {
                                it.copy(
                                    isLoadingPage = false
                                )
                            }
                        }
                        if (it.status == ResultStatus.FAILURE && isFirstError) {
                            onErrorCallBack("Ошибка получения данных. Используется кеш.")
                            isFirstError = false
                        }
                    }
                    ResultStatus.LOADING -> {}
                }
            }
        }
    }

    fun resetPaginationListHolder(onResetCallBack: () -> Unit) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    isLastPage = false,
                    isLoading = true,
                    isLoadingPage = false,
                    currentPage = 0
                )
            }
            isFirstError = true
            _paginationMatchInfoListHolder.emit(emptyList())
            onResetCallBack.invoke()
        }
    }
}