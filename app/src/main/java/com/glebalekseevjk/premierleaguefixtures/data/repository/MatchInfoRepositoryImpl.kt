package com.glebalekseevjk.premierleaguefixtures.data.repository

import androidx.lifecycle.asFlow
import com.glebalekseevjk.premierleaguefixtures.data.local.dao.MatchInfoDao
import com.glebalekseevjk.premierleaguefixtures.data.local.model.MatchInfoDbModel
import com.glebalekseevjk.premierleaguefixtures.data.remote.MatchInfoService
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ErrorType
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ResultType
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
    override fun getMatch(matchNumber: Int) =
        matchInfoDao.get(matchNumber).asFlow()
            .map {
                if (it != null)
                    ResultType.Success(mapper.mapDbModelToItem(it))
                else ResultType.Failure<Nothing>(ErrorType.Unknown)
            }


    override suspend fun searchTeamNamePagedMatchInfoList(
        teamName: String,
        page: Int
    ): ResultType<List<MatchInfo>> {
        return with(Dispatchers.IO) {
            ResultType.Success(
                matchInfoDao.searchTeamNamePagedMatchInfoList(
                    TOTAL_PER_PAGE,
                    page * TOTAL_PER_PAGE - TOTAL_PER_PAGE,
                    teamName
                ).map { mapper.mapDbModelToItem(it) }
            )
        }
    }


    override fun getMatchListRangeForPage(page: Int): Flow<ResultType<List<MatchInfo>>> =
        getMatchListRangeForPageFromNetwork(page).map {
            var errorType: ErrorType? = null
            when (it) {
                is ResultType.Failure<*> -> errorType = it.errorType
                is ResultType.Loading -> return@map it
                is ResultType.Success -> {
                    // добавляю с заменой существующих в хранилище. patch
                    val newList = it.data
                    matchInfoDao.addAll(*newList.map { mapper.mapItemToDbModel(it) }.toTypedArray())
                }
            }

            // Получение данных из локального хранилища. cache
            val list = matchInfoDao.getPagedMatchInfoList(
                TOTAL_PER_PAGE,
                page * TOTAL_PER_PAGE - TOTAL_PER_PAGE
            ).map { mapper.mapDbModelToItem(it) }
            return@map if (errorType != null) ResultType.Failure(errorType, list) else
                ResultType.Success(list)
        }.flowOn(Dispatchers.IO)

    private fun getMatchListRangeForPageFromNetwork(page: Int): Flow<ResultType<List<MatchInfo>>> =
        flow {
            emit(ResultType.Loading)

            val matchInfoListResponse = runCatching {
                matchInfoService.getTodoList().execute()
            }.getOrNull()

            val result = getResultFromMatchInfoListResponse(matchInfoListResponse)
            when (result) {
                is ResultType.Failure<*> -> emit(ResultType.Failure<Nothing>(ErrorType.Unknown))
                is ResultType.Loading -> {}
                is ResultType.Success -> {
                    emit(ResultType.Success(getRangeListForPage(result.data, page)))
                }
            }
        }.flowOn(Dispatchers.IO)

    private fun getResultFromMatchInfoListResponse(response: Response<List<MatchInfo>>?): ResultType<List<MatchInfo>> {
        response?.code().let {
            when (it) {
                200 -> {}
                else -> {
                    return ResultType.Failure<Nothing>(ErrorType.Unknown)
                }
            }
        }
        val list = response?.body()
        return if (list == null) {
            ResultType.Failure<Nothing>(ErrorType.Unknown)
        } else {
            ResultType.Success(list)
        }
    }

    private fun <T> getRangeListForPage(list: List<T>, page: Int): List<T> {
        val start = TOTAL_PER_PAGE * page - TOTAL_PER_PAGE
        var end = TOTAL_PER_PAGE * page - 1
        if (end >= list.size) end = list.size - 1
        val newList = list.subList(start, end + 1)
        return newList
    }
}