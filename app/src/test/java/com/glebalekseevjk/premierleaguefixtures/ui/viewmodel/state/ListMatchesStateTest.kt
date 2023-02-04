package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

import org.junit.Assert.*

import org.junit.Test

class ListMatchesStateTest {
    lateinit var state: ListMatchesState

    @Test
    fun `set state as Idle should be settled`() {
        state = ListMatchesState.Idle

        assertFalse(state.isStart())
        assertFalse(state.isNotFound())
    }

    @Test
    fun `set state as Start should be settled`() {
        state = ListMatchesState.Start

        assertTrue(state.isStart())
        assertFalse(state.isNotFound())
    }

    @Test
    fun `set state as NotFound should be settled`() {
        state = ListMatchesState.NotFound

        assertTrue(state.isNotFound())
        assertFalse(state.isStart())
    }
}