package com.glebalekseevjk.premierleaguefixtures

import android.app.Application
import com.glebalekseevjk.premierleaguefixtures.data.remote.RetrofitClient
import com.glebalekseevjk.premierleaguefixtures.data.repository.MatchInfoRepositoryImpl
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModelFactory
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.MatchDetailViewModelFactory

class MainApplication: Application() {
    private val matchInfoRepositoryImpl by lazy { MatchInfoRepositoryImpl(RetrofitClient.matchInfoApi) }
    val listMatchesViewModelFactory by lazy { ListMatchesViewModelFactory(MatchInfoUseCase(matchInfoRepositoryImpl)) }
    val matchDetailViewModelFactory by lazy { MatchDetailViewModelFactory(MatchInfoUseCase(matchInfoRepositoryImpl)) }
}