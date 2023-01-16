package com.glebalekseevjk.premierleaguefixtures.domain.interactor

import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import javax.inject.Inject

class MatchInfoUseCase @Inject constructor(private val matchInfoRepository: MatchInfoRepository) {
    suspend fun getMatch(matchNumber: Int) = matchInfoRepository.getMatch(matchNumber)
    suspend fun getMatchListRangeForPageLocal(page: Int) =
        matchInfoRepository.getMatchListRangeForPageLocal(page)

    suspend fun getMatchListRangeForPageRemote(page: Int) =
        matchInfoRepository.getMatchListRangeForPageRemote(page)

    suspend fun searchTeamNamePagedMatchInfoList(teamName: String, page: Int) =
        matchInfoRepository.searchTeamNamePagedMatchInfoList(teamName, page)
}