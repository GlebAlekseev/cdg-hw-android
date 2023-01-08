package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.asLiveData
import com.glebalekseevjk.premierleaguefixtures.data.repository.MatchInfoRepositoryImpl
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.MatchDetailState

class MatchDetailViewModel : BaseViewModel<MatchDetailState>(MatchDetailState()) {
    private val matchInfoRepository = MatchInfoRepositoryImpl()
    private val matchInfoUseCase = MatchInfoUseCase(matchInfoRepository)

    fun setCurrentMatchInfo(matchNumber: Int) {
        subscribeOnDataSource(
            matchInfoUseCase.getMatch(matchNumber).asLiveData()
        ) { response, state ->
            state.copy(
                matchInfo = response
            )
        }
    }
}