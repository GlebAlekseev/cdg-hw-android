package com.glebalekseevjk.premierleaguefixtures.domain.interactor

import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import javax.inject.Inject

class MatchInfoUseCase @Inject constructor(private val matchInfoRepository: MatchInfoRepository) {
    fun getMatch(matchNumber: Int) = matchInfoRepository.getMatch(matchNumber)
    fun getMatchListRangeForPage(page: Int) = matchInfoRepository.getMatchListRangeForPage(page)
    suspend fun searchTeamNamePagedMatchInfoList(teamName: String, page: Int) =
        matchInfoRepository.searchTeamNamePagedMatchInfoList(teamName, page)
}