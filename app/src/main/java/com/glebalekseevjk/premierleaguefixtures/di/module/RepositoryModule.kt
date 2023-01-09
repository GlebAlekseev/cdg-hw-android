package com.glebalekseevjk.premierleaguefixtures.di.module

import com.glebalekseevjk.premierleaguefixtures.data.repository.MatchInfoRepositoryImpl
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @AppComponentScope
    @Binds
    fun bindMatchInfoRepository(matchInfoRepositoryImpl: MatchInfoRepositoryImpl): MatchInfoRepository
}