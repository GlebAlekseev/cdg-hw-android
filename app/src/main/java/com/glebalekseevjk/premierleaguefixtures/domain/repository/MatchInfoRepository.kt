package com.glebalekseevjk.premierleaguefixtures.domain.repository

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Result
import kotlinx.coroutines.flow.Flow

interface MatchInfoRepository {
    fun getMatchListRangeForPage(page: Int): Flow<Result<List<MatchInfo>>>
    fun getMatch(matchNumber: Int): Flow<MatchInfo?>
    suspend fun searchTeamNamePagedMatchInfoList(teamName: String, page: Int): List<MatchInfo>
    companion object {
        const val TOTAL_PER_PAGE = 8
    }
}