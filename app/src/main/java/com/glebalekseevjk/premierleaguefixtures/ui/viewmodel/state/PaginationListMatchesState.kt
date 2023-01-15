package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo

// Start -> NeedLoading -> Loading -> NeedLoading || (Finish -> Empty)
sealed class PaginationListMatchesState {
    data class Start(val requestTeamName: String? = null) : PaginationListMatchesState()

    data class NeedLoading(
        override val prevState: PaginationListMatchesState = Start(),
        override val newList: List<MatchInfo> = emptyList()
    ) : PaginationListMatchesState(), HasNewList

    data class Loading(
        override val prevState: PaginationListMatchesState
    ) : PaginationListMatchesState(), HasPrev

    data class Finish(
        override val prevState: PaginationListMatchesState,
        override val newList: List<MatchInfo>
    ) :
        PaginationListMatchesState(), HasNewList

    data class Empty(override val prevState: PaginationListMatchesState) :
        PaginationListMatchesState(), HasPrev

    fun getPage(): Int {
        return when (this) {
            is Loading -> prevState.getPage() + 1
            is NeedLoading -> prevState.getPage()
            else -> 0
        }
    }

    fun getPaginationMatchList(): List<MatchInfo> {
        return when (this) {
            is HasNewList -> prevState.getPaginationMatchList() + newList
            is HasPrev -> prevState.getPaginationMatchList()
            else -> emptyList()
        }
    }

    fun getTeamName(): String {
        return when (this) {
            is Start -> requestTeamName ?: ""
            is HasPrev -> prevState.getTeamName()
            else -> ""
        }
    }

    val isNeedLoading
        get() = this is NeedLoading

    val isLoading
        get() = this is Loading

    val isFinish
        get() = this is Finish || this is Empty

    val isEmpty
        get() = this is Empty

    companion object {
        interface HasPrev {
            val prevState: PaginationListMatchesState
        }
        interface HasNewList: HasPrev {
            val newList: List<MatchInfo>
        }
    }
}

