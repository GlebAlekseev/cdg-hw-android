package com.glebalekseevjk.premierleaguefixtures.data.repository

import com.glebalekseevjk.premierleaguefixtures.data.local.dao.MatchInfoDao
import com.glebalekseevjk.premierleaguefixtures.data.local.model.MatchInfoDbModel
import com.glebalekseevjk.premierleaguefixtures.data.remote.MatchInfoService
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Resource
import com.glebalekseevjk.premierleaguefixtures.domain.mapper.Mapper
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository.Companion.TOTAL_PER_PAGE
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class MatchInfoRepositoryImpl @Inject constructor(
    private val matchInfoService: MatchInfoService,
    private val matchInfoDao: MatchInfoDao,
    private val mapper: Mapper<MatchInfo, MatchInfoDbModel>
) : MatchInfoRepository {
    override suspend fun getMatch(matchNumber: Int): Resource<MatchInfo> = with(Dispatchers.IO) {
        val result = matchInfoDao.get(matchNumber)
        if (result == null) {
            Resource.Failure<Nothing>(NoSuchElementException())
        } else {
            Resource.Success(mapper.mapDbModelToItem(result))
        }
    }

    override suspend fun searchTeamNamePagedMatchInfoList(
        teamName: String,
        page: Int
    ): Resource<List<MatchInfo>> = with(Dispatchers.IO) {
        Resource.Success(
            matchInfoDao.searchTeamNamePagedMatchInfoList(
                limit = TOTAL_PER_PAGE,
                offset = page * TOTAL_PER_PAGE - TOTAL_PER_PAGE,
                requestText = teamName
            ).map { mapper.mapDbModelToItem(it) }
        )
    }

    override suspend fun getMatchListRangeForPageLocal(page: Int): Resource<List<MatchInfo>> =
        with(Dispatchers.IO) {
            return@with Resource.Success(matchInfoDao.getPagedMatchInfoList(
                limit = TOTAL_PER_PAGE,
                offset = page * TOTAL_PER_PAGE - TOTAL_PER_PAGE
            ).map { mapper.mapDbModelToItem(it) })
        }

    override suspend fun getMatchListRangeForPageRemote(page: Int): Resource<List<MatchInfo>> =
        with(Dispatchers.IO) {
            val result = getMatchListRangeForPageFromNetwork(page)
            when (result) {
                is Resource.Success -> {
                    try {
                        val newList = result.data
                        matchInfoDao.addAll(*newList.map { mapper.mapItemToDbModel(it) }
                            .toTypedArray())
                    } catch (e: Exception) {
                        return@with Resource.Failure(e)
                    }
                }
                else -> {}
            }
            return@with result
        }

    private suspend fun getMatchListRangeForPageFromNetwork(page: Int): Resource<List<MatchInfo>> =
        with(Dispatchers.IO) {
            try {
                val matchInfoListResponse = matchInfoService.getTodoList()
                val result = getResultFromMatchInfoListResponse(matchInfoListResponse)
                return@with when (result) {
                    is Resource.Failure<*> -> result
                    is Resource.Success -> Resource.Success(getRangeListForPage(result.data, page))
                }
            } catch (e: Exception) {
                return@with Resource.Failure(e)
            }
        }


    private fun getResultFromMatchInfoListResponse(
        response: Response<List<MatchInfo>>
    ): Resource<List<MatchInfo>> {
        response.code().let {
            when (it) {
                200 -> {}
                else -> {
                    return Resource.Failure<Nothing>(HttpException(response))
                }
            }
        }
        val list = response.body()
        return if (list == null) {
            Resource.Failure<Nothing>(NullPointerException())
        } else {
            Resource.Success(list)
        }
    }

    private fun <T> getRangeListForPage(list: List<T>, page: Int): List<T> {
        var start = TOTAL_PER_PAGE * page - TOTAL_PER_PAGE
        var end = TOTAL_PER_PAGE * page - 1
        if (end >= list.size) end = list.size - 1
        if (end + 1 < start) start = end + 1
        return list.subList(start, end + 1)
    }
}