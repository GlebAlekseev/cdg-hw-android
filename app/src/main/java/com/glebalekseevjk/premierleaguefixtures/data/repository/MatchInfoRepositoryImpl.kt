package com.glebalekseevjk.premierleaguefixtures.data.repository

import androidx.lifecycle.asFlow
import com.glebalekseevjk.premierleaguefixtures.data.local.dao.MatchInfoDao
import com.glebalekseevjk.premierleaguefixtures.data.local.model.MatchInfoDbModel
import com.glebalekseevjk.premierleaguefixtures.data.remote.MatchInfoService
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Result
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ResultStatus
import com.glebalekseevjk.premierleaguefixtures.domain.mapper.Mapper
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository.Companion.TOTAL_PER_PAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class MatchInfoRepositoryImpl @Inject constructor(
    private val matchInfoService: MatchInfoService,
    private val matchInfoDao: MatchInfoDao,
    private val mapper: Mapper<MatchInfo, MatchInfoDbModel>
) : MatchInfoRepository {

    override fun getMatch(matchNumber: Int): Flow<MatchInfo?> =
        matchInfoDao.get(matchNumber).asFlow().map { it?.let { mapper.mapDbModelToItem(it) } }

    override suspend fun searchTeamNamePagedMatchInfoList(
        teamName: String,
        page: Int
    ): List<MatchInfo> {
        return with(Dispatchers.IO){
            matchInfoDao.searchTeamNamePagedMatchInfoList(
                TOTAL_PER_PAGE,
                page * TOTAL_PER_PAGE - TOTAL_PER_PAGE,
                teamName
            ).map { mapper.mapDbModelToItem(it) }
        }
    }

    override fun getMatchListRangeForPage(page: Int): Flow<Result<List<MatchInfo>>> =
        getMatchListRangeForPageFromNetwork(page).map {
            when (it.status) {
                ResultStatus.SUCCESS -> {
                    // добавляю с заменой существующих в хранилище. patch
                    val newList = it.data
                    matchInfoDao.addAll(*newList.map { mapper.mapItemToDbModel(it) }.toTypedArray())
                }
                ResultStatus.LOADING -> return@map it
                ResultStatus.FAILURE -> {}
            }

            // Получение данных из локального хранилища. cache
            val list = matchInfoDao.getPagedMatchInfoList(
                TOTAL_PER_PAGE,
                page * TOTAL_PER_PAGE - TOTAL_PER_PAGE
            ).map { mapper.mapDbModelToItem(it) }

            return@map Result(it.status, list)
        }.flowOn(Dispatchers.IO)

    private fun getMatchListRangeForPageFromNetwork(page: Int): Flow<Result<List<MatchInfo>>> =
        flow {
            emit(Result(ResultStatus.LOADING, emptyList()))
            val matchInfoListResponse = runCatching {
                matchInfoService.getTodoList().execute()
            }.getOrNull()
            val result = getResultFromMatchInfoListResponse(matchInfoListResponse)
            when (result.status) {
                ResultStatus.SUCCESS -> emit(
                    Result(
                        ResultStatus.SUCCESS,
                        getRangeListForPage(result.data, page)
                    )
                )
                else -> emit(result)
            }
        }.flowOn(Dispatchers.IO)

    private fun getResultFromMatchInfoListResponse(response: Response<List<MatchInfo>>?): Result<List<MatchInfo>> {
        var status = ResultStatus.SUCCESS
        var data = emptyList<MatchInfo>()
        response?.code().let {
            when (it) {
                200 -> {}
                else -> {
                    status = ResultStatus.FAILURE
                }
            }
        }
        val list = response?.body()
        if (list == null) {
            status = ResultStatus.FAILURE
        } else {
            data = list
        }
        return Result(status, data)
    }

    private fun <T> getRangeListForPage(list: List<T>, page: Int): List<T> {
        val start = TOTAL_PER_PAGE * page - TOTAL_PER_PAGE
        var end = TOTAL_PER_PAGE * page - 1
        if (end >= list.size) end = list.size - 1
        val newList = list.subList(start, end + 1)
        return newList
    }
}