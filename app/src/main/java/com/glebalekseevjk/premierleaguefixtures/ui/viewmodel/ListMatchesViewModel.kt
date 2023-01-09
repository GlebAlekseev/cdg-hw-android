package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.asLiveData
import com.glebalekseevjk.premierleaguefixtures.data.remote.RetrofitClient
import com.glebalekseevjk.premierleaguefixtures.data.repository.MatchInfoRepositoryImpl
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ResultStatus
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ListMatchesViewModel(
    private val matchInfoUseCase: MatchInfoUseCase
) : BaseViewModel<ListMatchesState>(ListMatchesState()) {

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

    suspend fun loadNextPage(onFinishCallback: ()->Unit = {}, onErrorCallBack: (String) -> Unit) {
        if (currentState.isLastPage) return
        if (!currentState.isLoading) {
            updateState {
                it.copy(
                    isLoading = true
                )
            }
        }
        val newPage = currentState.currentPage + 1
        updateState {
            it.copy(
                currentPage = newPage
            )
        }

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
                    _paginationMatchInfoListHolder.emit(_paginationMatchInfoListHolder.value + it.data)
                    if (it.status == ResultStatus.FAILURE) onErrorCallBack("Ошибка получения данных.")
                    onFinishCallback.invoke()
                }
                ResultStatus.LOADING -> {}
            }
        }
    }

    suspend fun resetPaginationListHolder(onResetCallBack: ()->Unit){
        _paginationMatchInfoListHolder.emit(emptyList())
        with(Dispatchers.Main){
            updateState {
                it.copy(
                    isLastPage = false,
                    isLoading = true,
                    currentPage = 0
                )
            }
        }
        onResetCallBack.invoke()
    }
}