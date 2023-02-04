package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import io.mockk.mockk
import org.junit.Assert.*

import org.junit.Test

class MatchDetailStateTest {
    lateinit var state: MatchDetailState

    @Test
    fun `set state as Loading should be settled`() {
        state = MatchDetailState.Loading

        assertEquals(state.toMatchInfo(), MatchInfo.MOCK)
    }

    @Test
    fun `set state as Error should be settled`() {
        state = MatchDetailState.Error("")

        assertEquals(state.toMatchInfo(), MatchInfo.MOCK)
    }
}