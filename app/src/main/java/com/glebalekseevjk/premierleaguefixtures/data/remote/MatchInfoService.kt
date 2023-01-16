package com.glebalekseevjk.premierleaguefixtures.data.remote

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface MatchInfoService {
    @GET("feed/json/epl-2021")
    suspend fun getTodoList(): Response<List<MatchInfo>>
}