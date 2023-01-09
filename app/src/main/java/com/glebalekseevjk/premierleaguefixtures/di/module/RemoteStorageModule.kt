package com.glebalekseevjk.premierleaguefixtures.di.module

import android.content.Context
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.data.remote.MatchInfoService
import com.glebalekseevjk.premierleaguefixtures.di.scope.AppComponentScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
interface RemoteStorageModule {
    companion object {
        @AppComponentScope
        @Provides
        fun provideRetrofitBuilder(context: Context): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(context.resources.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
        }

        @AppComponentScope
        @Provides
        fun provideMatchInfoService(
            retrofitBuilder: Retrofit.Builder
        ): MatchInfoService {
            return retrofitBuilder
                .build()
                .create(MatchInfoService::class.java)
        }
    }
}