package com.glebalekseevjk.premierleaguefixtures.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.glebalekseevjk.premierleaguefixtures.data.local.model.MatchInfoDbModel

@Dao
interface MatchInfoDao {
    @Query("SELECT * FROM MatchInfoDbModel WHERE matchNumber = :matchNumber")
    suspend fun get(matchNumber: Int): MatchInfoDbModel?

    @Query("SELECT * FROM MatchInfoDbModel LIMIT :limit OFFSET :offset")
    suspend fun getPagedMatchInfoList(limit: Int, offset: Int): List<MatchInfoDbModel>

    @Query("SELECT * FROM MatchInfoDbModel WHERE HomeTeam || AwayTeam " +
            "LIKE '%' || :requestText  || '%' LIMIT :limit OFFSET :offset")
    suspend fun searchTeamNamePagedMatchInfoList(
        limit: Int,
        offset: Int,
        requestText: String
    ): List<MatchInfoDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(vararg todoList: MatchInfoDbModel)
}