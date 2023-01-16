package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

sealed class SearchListMatchesState {
    object Start : SearchListMatchesState()
    object Loading : SearchListMatchesState()
    object Idle : SearchListMatchesState()
    object NotFound : SearchListMatchesState()

    fun isNotFound() = this is NotFound
    fun isLoading() = this is Loading
}