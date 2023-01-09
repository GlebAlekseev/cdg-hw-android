package com.glebalekseevjk.premierleaguefixtures.di.module

import android.content.Context
import androidx.room.Room
import com.glebalekseevjk.premierleaguefixtures.data.local.AppDatabase
import com.glebalekseevjk.premierleaguefixtures.data.local.dao.MatchInfoDao
import com.glebalekseevjk.premierleaguefixtures.data.local.model.MatchInfoDbModel
import com.glebalekseevjk.premierleaguefixtures.data.mapper.MatchInfoMapperImpl
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.mapper.Mapper
import dagger.Binds
import dagger.Module
import dagger.Provides


@Module
interface LocalStorageModule {
    @AppComponentScope
    @Binds
    fun provideMapperMatchInfo(matchInfoMapperImpl: MatchInfoMapperImpl): Mapper<MatchInfo, MatchInfoDbModel>

    companion object {
        @AppComponentScope
        @Provides
        fun provideAppDataBase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build()
        }

        @AppComponentScope
        @Provides
        fun provideMatchInfoDao(appDatabase: AppDatabase): MatchInfoDao {
            return appDatabase.matchInfoDao()
        }
    }
}