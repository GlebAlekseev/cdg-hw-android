package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.ListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.SearchListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.SearchListMatchesState
import com.glebalekseevjk.premierleaguefixtures.utils.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchListMatchesViewModelTest {
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    lateinit var viewModel: SearchListMatchesViewModel

    @MockK
    lateinit var matchInfoUseCase: MatchInfoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = SearchListMatchesViewModel(
            matchInfoUseCase = matchInfoUseCase
        )
    }

    @Test
    fun `state should be set to Idle`() = runTest {
        viewModel.userIntent.send(SearchListMatchesIntent.SetIdleState)

        assertTrue(viewModel.searchListMatchesState.value is SearchListMatchesState.Idle)
        assertFalse(viewModel.searchListMatchesState.value is SearchListMatchesState.Start)
        assertFalse(viewModel.searchListMatchesState.value is SearchListMatchesState.NotFound)
        assertFalse(viewModel.searchListMatchesState.value is SearchListMatchesState.Loading)
    }

    @Test
    fun `state should be set to NotFound`() = runTest {
        viewModel.userIntent.send(SearchListMatchesIntent.SetNotFoundState)

        assertTrue(viewModel.searchListMatchesState.value is SearchListMatchesState.NotFound)
        assertFalse(viewModel.searchListMatchesState.value is SearchListMatchesState.Idle)
        assertFalse(viewModel.searchListMatchesState.value is SearchListMatchesState.Start)
        assertFalse(viewModel.searchListMatchesState.value is SearchListMatchesState.Loading)
    }

    @Test
    fun `state should be set to Loading`() = runTest {
        viewModel.userIntent.send(SearchListMatchesIntent.SetLoading)

        assertTrue(viewModel.searchListMatchesState.value is SearchListMatchesState.Loading)
        assertFalse(viewModel.searchListMatchesState.value is SearchListMatchesState.Idle)
        assertFalse(viewModel.searchListMatchesState.value is SearchListMatchesState.Start)
        assertFalse(viewModel.searchListMatchesState.value is SearchListMatchesState.NotFound)
    }

    @Test
    fun `the team name will be set`() = runTest {
        val exampleTeamName = "teamName"

        viewModel.userIntent.send(SearchListMatchesIntent.SetTeamName(exampleTeamName))

        assertEquals(viewModel.teamName.value, exampleTeamName)
    }

    @Test
    fun `layout manager should switch`() = runTest {
        val pre = viewModel.layoutManagerState.value

        viewModel.userIntent.send(SearchListMatchesIntent.ToggleLayoutManagerState{})

        assertNotEquals(pre, viewModel.layoutManagerState.value)
    }
}