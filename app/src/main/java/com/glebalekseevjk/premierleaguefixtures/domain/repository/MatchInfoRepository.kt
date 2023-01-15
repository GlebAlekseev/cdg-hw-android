package com.glebalekseevjk.premierleaguefixtures.domain.repository

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ResultType
import kotlinx.coroutines.flow.Flow

interface MatchInfoRepository {
    fun getMatchListRangeForPage(page: Int): Flow<ResultType<List<MatchInfo>>>
    fun getMatch(matchNumber: Int): Flow<ResultType<MatchInfo>>
    suspend fun searchTeamNamePagedMatchInfoList(teamName: String, page: Int): ResultType<List<MatchInfo>>

    companion object {
        const val TOTAL_PER_PAGE = 8
    }
}