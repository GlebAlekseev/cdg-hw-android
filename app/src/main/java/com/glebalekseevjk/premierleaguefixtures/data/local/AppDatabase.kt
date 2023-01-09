package com.glebalekseevjk.premierleaguefixtures.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.glebalekseevjk.premierleaguefixtures.data.local.dao.MatchInfoDao
import com.glebalekseevjk.premierleaguefixtures.data.local.model.MatchInfoDbModel

@Database(entities = [MatchInfoDbModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "premier-league-fixtures-database"
        fun getDataBase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

    abstract fun matchInfoDao(): MatchInfoDao
}