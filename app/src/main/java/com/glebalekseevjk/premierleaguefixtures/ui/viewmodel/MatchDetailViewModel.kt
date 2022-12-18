package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.premierleaguefixtures.data.repository.MatchInfoRepositoryImpl
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MatchDetailViewModel: ViewModel() {
    private val matchInfoRepository = MatchInfoRepositoryImpl()
    private val matchInfoUseCase = MatchInfoUseCase(matchInfoRepository)

    val currentMatchNumber = MutableStateFlow(BAD_MATCH_NUMBER)

    fun observeCurrentMatchList(): Flow<List<MatchInfo>> {
        if (currentMatchNumber.value != BAD_MATCH_NUMBER){
            return matchInfoUseCase.getMatch(currentMatchNumber.value)
        } else {
            throw RuntimeException("Bad match number ${currentMatchNumber.value}")
        }
    }

    companion object {
        const val BAD_MATCH_NUMBER = -1
    }
}