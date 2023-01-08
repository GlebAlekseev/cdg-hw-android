package com.glebalekseevjk.premierleaguefixtures.data.repository

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

class MatchInfoRepositoryImpl: MatchInfoRepository {
    private val _mockList: MutableStateFlow<List<MatchInfo>> = MutableStateFlow(emptyList())
    val mockList: StateFlow<List<MatchInfo>>
        get() = _mockList

    override fun getPaginationMatchList(): Flow<List<MatchInfo>> = mockList

    // Вернет true, если последняя страница
    override suspend fun addMatchListForPage(page: Int, callback: () -> Unit): Boolean {
        val start = TOTAL_PER_PAGE * page - TOTAL_PER_PAGE
        var end = TOTAL_PER_PAGE * page - 1
        val isLast = MatchInfoRepositoryImpl.mockList.size - 1 <= end
        if (end >= MatchInfoRepositoryImpl.mockList.size)  end = MatchInfoRepositoryImpl.mockList.size - 1
        try {
            val list = MatchInfoRepositoryImpl.mockList.subList(start, end + 1)
            callback.invoke()
            _mockList.emit(_mockList.value + list)
        }catch (err: Exception){
            return true
        }
        return isLast
    }

    override fun getMatch(matchNumber: Int): Flow<MatchInfo> = flow {
        val matchInfo = MatchInfoRepositoryImpl.mockList.firstOrNull { it.matchNumber == matchNumber } ?: throw RuntimeException("There is no match with this number")
        emit(matchInfo)
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val TOTAL_PER_PAGE = 8
        val mockList: List<MatchInfo> = listOf(
            MatchInfo(
                1,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge 1",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                2,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge 2",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                3,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge 3",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                4,
                1,
                "2022-12-18 14:00:00Z",
                "Stamford Bridge 4",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                5,
                1,
                "2021-08-14 14:00:00Z",
                "Stamford Bridge 5",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                6,
                1,
                "2021-08-14 14:00:00Z",
                "Stamford Bridge 6",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                7,
                1,
                "2021-08-14 14:00:00Z",
                "Stamford Bridge 7",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                8,
                1,
                "2022-08-14 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                9,
                1,
                "2022-08-14 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                10,
                1,
                "2021-08-14 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                11,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                12,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                13,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                14,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                15,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                16,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                17,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                18,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                19,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                20,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                21,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                22,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                23,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                24,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                25,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                26,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                27,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                28,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                29,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                30,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                31,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                32,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                33,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                34,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                35,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                36,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                37,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                38,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                39,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                40,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                41,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                42,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                43,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
            MatchInfo(
                44,
                1,
                "2021-08-18 14:00:00Z",
                "Stamford Bridge",
                "Chelsea",
                "Crystal Palace",
                null,
                3,
                0
            ),
        )
    }
}