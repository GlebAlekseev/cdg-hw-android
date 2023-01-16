package com.glebalekseevjk.premierleaguefixtures.domain.repository

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Resource
import kotlinx.coroutines.flow.Flow

interface MatchInfoRepository {
    suspend fun getMatchListRangeForPageLocal(page: Int): Resource<List<MatchInfo>>
    suspend fun getMatchListRangeForPageRemote(page: Int): Resource<List<MatchInfo>>
    suspend fun getMatch(matchNumber: Int): Resource<MatchInfo>
    suspend fun searchTeamNamePagedMatchInfoList(
        teamName: String,
        page: Int
    ): Resource<List<MatchInfo>>

    companion object {
        const val TOTAL_PER_PAGE = 16
    }
}