package com.glebalekseevjk.premierleaguefixtures.domain.repository

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MatchInfoRepository {
    fun getMatchListRangeForPage(page: Int): Flow<Result<List<MatchInfo>>>
    fun getMatch(matchNumber: Int): Flow<MatchInfo?>
    companion object {
        const val TOTAL_PER_PAGE = 8
    }
}