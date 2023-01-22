package com.glebalekseevjk.premierleaguefixtures.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.premierleaguefixtures.di.ViewModelKey
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.MatchDetailViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.SearchListMatchesViewModel
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ListMatchesViewModel::class)
    fun bindListMatchesViewModel(listMatchesViewModel: ListMatchesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MatchDetailViewModel::class)
    fun bindMatchDetailViewModel(todoViewModel: MatchDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchListMatchesViewModel::class)
    fun bindSearchListMatchesViewModel(searchListMatchesViewModel: SearchListMatchesViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}