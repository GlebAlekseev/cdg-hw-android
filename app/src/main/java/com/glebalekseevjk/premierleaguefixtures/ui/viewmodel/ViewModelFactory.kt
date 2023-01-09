package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase


class ListMatchesViewModelFactory(
    private val matchInfoUseCase: MatchInfoUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListMatchesViewModel(matchInfoUseCase) as T
    }
}

class MatchDetailViewModelFactory(
    private val matchInfoUseCase: MatchInfoUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MatchDetailViewModel(matchInfoUseCase) as T
    }
}
