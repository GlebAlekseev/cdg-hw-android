package com.glebalekseevjk.premierleaguefixtures.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Resource
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import kotlinx.coroutines.flow.collect
import java.io.IOException
import javax.inject.Inject

class ListMatchesDataSource @Inject constructor(
    private val matchInfoUseCase: MatchInfoUseCase
) : PagingSource<Int, MatchInfo>() {
    var isLocal = false

    override fun getRefreshKey(state: PagingState<Int, MatchInfo>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchInfo> {
        val nextPageNumber = params.key ?: STARTING_PAGE_INDEX
        val response = if (isLocal) {
            matchInfoUseCase.getMatchListRangeForPageLocal(nextPageNumber)
        } else {
            matchInfoUseCase.getMatchListRangeForPageRemote(nextPageNumber)
        }
        return when (response) {
            is Resource.Failure -> LoadResult.Error(response.throwable)
            is Resource.Success -> {
                val isLast = response.data.size < MatchInfoRepository.TOTAL_PER_PAGE
                LoadResult.Page(
                    prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                    nextKey = if (isLast) null else nextPageNumber.plus(1),
                    data = response.data
                )
            }
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}