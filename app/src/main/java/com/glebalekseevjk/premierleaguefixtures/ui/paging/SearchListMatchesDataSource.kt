package com.glebalekseevjk.premierleaguefixtures.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Resource
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.domain.repository.MatchInfoRepository
import javax.inject.Inject

class SearchListMatchesDataSource @Inject constructor(
    private val matchInfoUseCase: MatchInfoUseCase,
    private val teamName: String
) : PagingSource<Int, MatchInfo>() {

    override fun getRefreshKey(state: PagingState<Int, MatchInfo>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchInfo> {
        val nextPageNumber = params.key ?: STARTING_PAGE_INDEX
        val response = matchInfoUseCase.searchTeamNamePagedMatchInfoList(teamName, nextPageNumber)
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