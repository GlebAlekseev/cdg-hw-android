package com.glebalekseevjk.premierleaguefixtures.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.glebalekseevjk.premierleaguefixtures.data.local.dao.MatchInfoDao
import com.glebalekseevjk.premierleaguefixtures.data.local.model.MatchInfoDbModel

@Database(entities = [MatchInfoDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun matchInfoDao(): MatchInfoDao

    companion object {
        const val DATABASE_NAME = "premier-league-fixtures-database"
    }
}