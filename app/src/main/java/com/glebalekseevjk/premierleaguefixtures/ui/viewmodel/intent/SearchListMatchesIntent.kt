package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent

sealed class SearchListMatchesIntent {
    data class ToggleLayoutManagerState(val callback: (drawableId: Int) -> Unit) :
        SearchListMatchesIntent()

    data class SetTeamName(val teamName: String) : SearchListMatchesIntent()
    object SetIdleState : SearchListMatchesIntent()
    object SetNotFoundState : SearchListMatchesIntent()
    object SetLoading : SearchListMatchesIntent()
}