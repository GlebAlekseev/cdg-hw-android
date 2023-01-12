package com.glebalekseevjk.premierleaguefixtures.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.glebalekseevjk.premierleaguefixtures.data.local.model.MatchInfoDbModel

@Dao
interface MatchInfoDao {
    @Query("SELECT * FROM MatchInfoDbModel WHERE matchNumber = :matchNumber")
    fun get(matchNumber: Int): LiveData<MatchInfoDbModel?>

    @Query("SELECT * FROM MatchInfoDbModel LIMIT :limit OFFSET :offset")
    fun getPagedMatchInfoList(limit: Int, offset: Int): List<MatchInfoDbModel>

    @Query("SELECT * FROM MatchInfoDbModel WHERE HomeTeam || AwayTeam LIKE '%' || :requestText  || '%' LIMIT :limit OFFSET :offset")
    suspend fun searchTeamNamePagedMatchInfoList(limit: Int, offset: Int, requestText: String): List<MatchInfoDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(vararg todoList: MatchInfoDbModel)
}