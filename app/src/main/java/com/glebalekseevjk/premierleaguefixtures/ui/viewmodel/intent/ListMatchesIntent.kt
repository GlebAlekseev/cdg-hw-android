package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent

sealed class ListMatchesIntent {
    data class ToggleLayoutManagerState(val callback: (drawableId: Int) -> Unit) :
        ListMatchesIntent()

    object EnableCacheMode : ListMatchesIntent()
    object SetIdleState : ListMatchesIntent()
    object SetNotFoundState : ListMatchesIntent()
}