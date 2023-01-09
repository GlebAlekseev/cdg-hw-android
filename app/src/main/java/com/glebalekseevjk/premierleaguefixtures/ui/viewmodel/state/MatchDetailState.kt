package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo

data class MatchDetailState(
    val matchInfo: MatchInfo = MatchInfo.MOCK
)