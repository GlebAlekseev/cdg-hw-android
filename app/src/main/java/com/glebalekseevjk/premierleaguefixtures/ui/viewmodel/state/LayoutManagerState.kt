package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

sealed class LayoutManagerState {
    object ViewTypeGrid : LayoutManagerState()
    object ViewTypeList : LayoutManagerState()
}