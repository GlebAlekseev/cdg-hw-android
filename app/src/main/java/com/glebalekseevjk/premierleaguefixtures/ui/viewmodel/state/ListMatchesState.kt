package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

sealed class ListMatchesState {
    object Start : ListMatchesState()
    object Idle : ListMatchesState()
    object NotFound : ListMatchesState()

    fun isNotFound() = this is NotFound
    fun isStart() = this is Start
}