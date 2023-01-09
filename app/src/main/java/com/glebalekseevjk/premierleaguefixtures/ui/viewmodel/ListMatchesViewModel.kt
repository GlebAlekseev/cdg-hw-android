package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import com.glebalekseevjk.premierleaguefixtures.data.repository.MatchInfoRepositoryImpl
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

class ListMatchesViewModel : BaseViewModel<ListMatchesState>(ListMatchesState()) {
    private val matchInfoRepository = MatchInfoRepositoryImpl()
    private val matchInfoUseCase = MatchInfoUseCase(matchInfoRepository)

    init {
        subscribeOnDataSource(
            matchInfoUseCase.getPaginationMatchList().asLiveData()
        ) { response, state ->
            state.copy(
                listMatches = response
            )
        }
    }

    suspend fun loadNextPage() {
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

        val isLast = with(Dispatchers.IO){
            delay(600)
            matchInfoUseCase.addMatchListForPage(newPage) {
                updateState {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }

        updateState {
            it.copy(
                isLoading = !isLast,
                isLastPage = isLast
            )
        }
    }
}