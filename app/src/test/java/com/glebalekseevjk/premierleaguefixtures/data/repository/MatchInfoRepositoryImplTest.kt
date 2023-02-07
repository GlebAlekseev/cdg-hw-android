package com.glebalekseevjk.premierleaguefixtures.data.repository

import com.glebalekseevjk.premierleaguefixtures.data.local.dao.MatchInfoDao
import com.glebalekseevjk.premierleaguefixtures.data.mapper.MatchInfoMapperImpl
import com.glebalekseevjk.premierleaguefixtures.data.remote.MatchInfoService
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Resource
import com.glebalekseevjk.premierleaguefixtures.utils.CoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MatchInfoRepositoryImplTest {
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    lateinit var matchInfoRepositoryImpl: MatchInfoRepositoryImpl

    @MockK
    lateinit var matchInfoService: MatchInfoService

    @MockK
    lateinit var matchInfoDao: MatchInfoDao

    private val mapperImpl = MatchInfoMapperImpl()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

//        every { matchInfoDao.getPagedMatchInfoList() }
//        every { matchInfoDao.addAll() }
//        every { matchInfoDao.searchTeamNamePagedMatchInfoList() }
//
//        every { matchInfoService.getTodoList() }

        matchInfoRepositoryImpl = MatchInfoRepositoryImpl(
            matchInfoService = matchInfoService,
            matchInfoDao = matchInfoDao,
            mapper = mapperImpl
        )
    }


    @Test
    fun `get match should return ResourceSuccess`() = runTest {
        val mockMatchItem = mapperImpl.mapItemToDbModel(MatchInfo.MOCK)
        coEvery { matchInfoDao.get(any()) } returns mockMatchItem
        matchInfoRepositoryImpl = MatchInfoRepositoryImpl(
            matchInfoService = matchInfoService,
            matchInfoDao = matchInfoDao,
            mapper = mapperImpl
        )

        val result = matchInfoRepositoryImpl.getMatch(1)

        assertEquals(result, Resource.Success(MatchInfo.MOCK))
    }

    @Test
    fun `get match should return ResourceFailure`() = runTest {
        coEvery { matchInfoDao.get(any()) } returns null
        matchInfoRepositoryImpl = MatchInfoRepositoryImpl(
            matchInfoService = matchInfoService,
            matchInfoDao = matchInfoDao,
            mapper = mapperImpl
        )

        val result = matchInfoRepositoryImpl.getMatch(1)

        assertTrue(result is Resource.Failure)
    }


    @Test
    fun `search team name paged match info list should return ResourceSuccess`() = runTest {
        coEvery {
            matchInfoDao.searchTeamNamePagedMatchInfoList(
                any(),
                any(),
                any()
            )
        } returns listOf()
        matchInfoRepositoryImpl = MatchInfoRepositoryImpl(
            matchInfoService = matchInfoService,
            matchInfoDao = matchInfoDao,
            mapper = mapperImpl
        )

        val result = matchInfoRepositoryImpl.searchTeamNamePagedMatchInfoList("teamName", 99)

        assertTrue(result is Resource.Success)
    }

    @Test
    fun `get match list range for page local should return ResourceSuccess`() = runTest {
        coEvery { matchInfoDao.getPagedMatchInfoList(any(), any()) } returns listOf()
        matchInfoRepositoryImpl = MatchInfoRepositoryImpl(
            matchInfoService = matchInfoService,
            matchInfoDao = matchInfoDao,
            mapper = mapperImpl
        )

        val result = matchInfoRepositoryImpl.searchTeamNamePagedMatchInfoList("teamName", 99)

        assertTrue(result is Resource.Success)
    }

    @Test
    fun getMatchListRangeForPageRemote() = runTest {
        coEvery { matchInfoDao.addAll(any(), any()) } just runs
        val mockResponse = mockk<Response<List<MatchInfo>>>()
//        every { mockResponse.code() } returns 200
        every { mockResponse.code() } returns 500
        every { mockResponse.message() } returns "fake message"
//        every { mockResponse.body() } returns listOf()
//        every { mockResponse.body() } returns null
        coEvery { matchInfoService.getTodoList() } returns mockResponse
        matchInfoRepositoryImpl = MatchInfoRepositoryImpl(
            matchInfoService = matchInfoService,
            matchInfoDao = matchInfoDao,
            mapper = mapperImpl
        )

        val result = matchInfoRepositoryImpl.getMatchListRangeForPageRemote(99)

        assertTrue(result is Resource.Failure && result.throwable::class.java == HttpException::class.java )
    }


    @Test
    fun getMatchListRangeForPageRemote2() = runTest {
        coEvery { matchInfoDao.addAll(any(), any()) } just runs
        val mockResponse = mockk<Response<List<MatchInfo>>>()
//        every { mockResponse.code() } returns 200
        every { mockResponse.code() } returns 200
//        every { mockResponse.message() } returns "fake message"
//        every { mockResponse.body() } returns listOf()
        every { mockResponse.body() } returns null
        coEvery { matchInfoService.getTodoList() } returns mockResponse
        matchInfoRepositoryImpl = MatchInfoRepositoryImpl(
            matchInfoService = matchInfoService,
            matchInfoDao = matchInfoDao,
            mapper = mapperImpl
        )

        val result = matchInfoRepositoryImpl.getMatchListRangeForPageRemote(99)

        assertTrue(result is Resource.Failure && result.throwable::class.java == NullPointerException::class.java )
    }

}