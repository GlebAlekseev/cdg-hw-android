package com.glebalekseevjk.premierleaguefixtures.data.repository

import com.glebalekseevjk.premierleaguefixtures.data.remote.MatchInfoService
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ResultStatus
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Result
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository.Companion.TOTAL_PER_PAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Response

class MatchInfoRepositoryImpl(
    private val matchInfoService: MatchInfoService
) : MatchInfoRepository {
    // Эмуляция локального хранилища
    private val _matchListLocalStore: MutableStateFlow<List<MatchInfo>> =
        MutableStateFlow(emptyList())

    override fun getMatch(matchNumber: Int): Flow<MatchInfo?> =
        _matchListLocalStore.map {
            it.firstOrNull { it.matchNumber == matchNumber } }

    override fun getMatchListRangeForPage(page: Int): Flow<Result<List<MatchInfo>>> = getMatchListRangeForPageFromNetwork(page).map {
        when (it.status) {
            ResultStatus.SUCCESS -> {
                // добавляю с заменой существующих в хранилище. patch
                val currentLocalList = _matchListLocalStore.value.toMutableList()
                val newList = it.data
                newList.forEach { matchInfo ->
                    if (currentLocalList.contains(matchInfo)) {
                        val old =
                            currentLocalList.find { it.matchNumber == matchInfo.matchNumber }
                        val indexOld = currentLocalList.indexOf(old)
                        currentLocalList[indexOld] = matchInfo
                    } else {
                        currentLocalList.add(matchInfo)
                    }
                }
                _matchListLocalStore.emit(currentLocalList)

                // Получение данных из локального хранилища. cache
                val list = getRangeListForPage(_matchListLocalStore.value, page)
                return@map Result(it.status, list)
            }
            ResultStatus.LOADING -> return@map it
            ResultStatus.FAILURE -> {
                val list = getRangeListForPage(_matchListLocalStore.value, page)
                return@map Result(it.status, list)
            }
        }
    }



    private fun getMatchListRangeForPageFromNetwork(page: Int): Flow<Result<List<MatchInfo>>> =
        flow {
            emit(Result(ResultStatus.LOADING, emptyList()))
            val matchInfoListResponse = runCatching {
                matchInfoService.getTodoList().execute()
            }.getOrNull()
            val result = getResultFromMatchInfoListResponse(matchInfoListResponse)
            when (result.status) {
                ResultStatus.SUCCESS -> emit(Result(
                    ResultStatus.SUCCESS,
                    getRangeListForPage(result.data, page)
                ))
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