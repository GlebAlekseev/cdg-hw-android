package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.asLiveData
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.MatchDetailState

class MatchDetailViewModel(
    private val matchInfoUseCase: MatchInfoUseCase
) : BaseViewModel<MatchDetailState>(MatchDetailState()) {

    fun setCurrentMatchInfo(matchNumber: Int) {
        subscribeOnDataSource(
            matchInfoUseCase.getMatch(matchNumber).asLiveData()
        ) { response, state ->
            state.copy(
                matchInfo = response
                    ?: throw RuntimeException("Attempt to get a non-existent element")
            )
        }
    }
}