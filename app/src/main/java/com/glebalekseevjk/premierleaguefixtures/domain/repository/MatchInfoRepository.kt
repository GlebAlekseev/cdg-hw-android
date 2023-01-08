package com.glebalekseevjk.premierleaguefixtures.domain.repository

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import kotlinx.coroutines.flow.Flow

interface MatchInfoRepository {
    fun getPaginationMatchList(): Flow<List<MatchInfo>>
    suspend fun addMatchListForPage(page: Int, callback: ()->Unit): Boolean
    fun getMatch(matchNumber: Int): Flow<MatchInfo>
}