package com.glebalekseevjk.premierleaguefixtures.data.repository

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class MatchInfoRepositoryImpl: MatchInfoRepository {
    private val _mockList: MutableStateFlow<List<MatchInfo>> = MutableStateFlow(MatchInfoRepositoryImpl.mockList)
    val mockList: StateFlow<List<MatchInfo>>
        get() = _mockList

    override fun getMatchList(): Flow<List<MatchInfo>> = mockList

    override fun getMatch(matchNumber: Int): Flow<List<MatchInfo>> = flow<List<MatchInfo>> {
        mockList.collect{ list ->
            val result = list.firstOrNull { it.matchNumber == matchNumber }
            if (result == null) emit(emptyList()) else emit(listOf(result))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
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
        )
    }
}