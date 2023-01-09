package com.glebalekseevjk.premierleaguefixtures.di

import android.content.Context
import com.glebalekseevjk.premierleaguefixtures.MainApplication
import com.glebalekseevjk.premierleaguefixtures.di.module.LocalStorageModule
import com.glebalekseevjk.premierleaguefixtures.di.module.RemoteStorageModule
import com.glebalekseevjk.premierleaguefixtures.di.module.RepositoryModule
import com.glebalekseevjk.premierleaguefixtures.di.module.ViewModelModule
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import dagger.BindsInstance
import dagger.Component

@AppComponentScope
@Component(modules = [RepositoryModule::class, LocalStorageModule::class, RemoteStorageModule::class, ViewModelModule::class])
interface AppComponent {
    fun injectMainApplication(application: MainApplication)
    fun createListMatchesFragmentSubcomponent(): ListMatchesFragmentSubcomponent
    fun createMatchDetailFragmentSubcomponent(): MatchDetailFragmentSubcomponent

    @Component.Factory
    interface Builder {
        fun create(@BindsInstance context: Context): AppComponent
    }
}