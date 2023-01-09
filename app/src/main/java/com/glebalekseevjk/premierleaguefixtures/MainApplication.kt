package com.glebalekseevjk.premierleaguefixtures

import android.app.Application
import com.glebalekseevjk.premierleaguefixtures.data.local.AppDatabase
import com.glebalekseevjk.premierleaguefixtures.data.mapper.MatchInfoMapperImpl
import com.glebalekseevjk.premierleaguefixtures.data.remote.RetrofitClient
import com.glebalekseevjk.premierleaguefixtures.data.repository.MatchInfoRepositoryImpl
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModelFactory
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.MatchDetailViewModelFactory

class MainApplication : Application() {
    private val appDatabase by lazy {
        AppDatabase.getDataBase(this)
    }
    private val matchInfoMapperImpl by lazy {
        MatchInfoMapperImpl()
    }
    private val matchInfoRepositoryImpl by lazy {
        MatchInfoRepositoryImpl(
            RetrofitClient.matchInfoApi,
            appDatabase.matchInfoDao(),
            matchInfoMapperImpl
        )
    }
    val listMatchesViewModelFactory by lazy {
        ListMatchesViewModelFactory(
            MatchInfoUseCase(
                matchInfoRepositoryImpl
            )
        )
    }
    val matchDetailViewModelFactory by lazy {
        MatchDetailViewModelFactory(
            MatchInfoUseCase(
                matchInfoRepositoryImpl
            )
        )
    }

}