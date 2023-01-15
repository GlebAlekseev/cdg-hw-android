package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo

sealed class MatchDetailState {
    object Loading : MatchDetailState()
    data class Loaded(val matchInfo: MatchInfo) : MatchDetailState()
    data class Error<T>(val errorMessage: T) : MatchDetailState()

    fun toMatchInfo(): MatchInfo{
        return when(this){
            is Loaded -> matchInfo
            else -> MatchInfo.MOCK
        }
    }
}