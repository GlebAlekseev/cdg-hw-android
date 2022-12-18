package com.glebalekseevjk.premierleaguefixtures.domain.repository

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import kotlinx.coroutines.flow.Flow

interface MatchInfoRepository {
    fun getMatchList(): Flow<List<MatchInfo>>
    fun getMatch(matchNumber: Int): Flow<List<MatchInfo>>
}