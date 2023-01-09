package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import javax.inject.Inject
import javax.inject.Provider


@Suppress("UNCHECKED_CAST")
@AppComponentScope
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}