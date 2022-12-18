package com.glebalekseevjk.premierleaguefixtures.domain.interactor

import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository

class MatchInfoUseCase(private val matchInfoRepository: MatchInfoRepository) {
    fun getMatch(matchNumber: Int) = matchInfoRepository.getMatch(matchNumber)
    fun getMatchList() = matchInfoRepository.getMatchList()
}