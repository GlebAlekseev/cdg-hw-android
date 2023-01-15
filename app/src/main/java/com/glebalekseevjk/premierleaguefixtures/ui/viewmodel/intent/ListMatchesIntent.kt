package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent

import com.glebalekseevjk.premierleaguefixtures.domain.entity.ErrorType

sealed class ListMatchesIntent {
    data class ToggleLayoutManagerState(val callback: (drawableId: Int) -> Unit) : ListMatchesIntent()
    data class LoadNextPage(val onErrorCallBack: (ErrorType) -> Unit) : ListMatchesIntent()
    object LoadNextPageFromLocalForRequest : ListMatchesIntent()
    object StopLoading : ListMatchesIntent()
    data class ResetPaginationListHolder(val teamName: String? = null, val onResetCallBack: () -> Unit) : ListMatchesIntent()
}