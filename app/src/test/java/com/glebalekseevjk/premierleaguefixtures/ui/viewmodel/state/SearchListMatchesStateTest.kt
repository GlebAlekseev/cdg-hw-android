package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state

import org.junit.Assert.*

import org.junit.Test

class SearchListMatchesStateTest {
    lateinit var state: SearchListMatchesState

    @Test
    fun `set state as Idle should be settled`() {
        state = SearchListMatchesState.Idle

        assertFalse(state.isLoading())
        assertFalse(state.isNotFound())
    }

    @Test
    fun `set state as Start should be settled`() {
        state = SearchListMatchesState.Start

        assertFalse(state.isLoading())
        assertFalse(state.isNotFound())
    }

    @Test
    fun `set state as NotFound should be settled`() {
        state = SearchListMatchesState.NotFound

        assertFalse(state.isLoading())
        assertTrue(state.isNotFound())
    }

    @Test
    fun `set state as Loading should be settled`() {
        state = SearchListMatchesState.Loading

        assertTrue(state.isLoading())
        assertFalse(state.isNotFound())
    }
}