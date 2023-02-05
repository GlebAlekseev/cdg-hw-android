package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.intent.ListMatchesIntent
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.ListMatchesState
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.MatchDetailState
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
class ListMatchesViewModelTest {
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    lateinit var viewModel: ListMatchesViewModel

    @MockK
    lateinit var matchInfoUseCase: MatchInfoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = ListMatchesViewModel(
            matchInfoUseCase = matchInfoUseCase
        )
    }

    @Test
    fun `state should be set to Idle`() = runTest {
        viewModel.userIntent.send(ListMatchesIntent.SetIdleState)

        assertTrue(viewModel.listMatchesState.value is ListMatchesState.Idle)
        assertFalse(viewModel.listMatchesState.value is ListMatchesState.Start)
        assertFalse(viewModel.listMatchesState.value is ListMatchesState.NotFound)
    }

    @Test
    fun `state should be set to NotFound`() = runTest {
        viewModel.userIntent.send(ListMatchesIntent.SetNotFoundState)

        assertTrue(viewModel.listMatchesState.value is ListMatchesState.NotFound)
        assertFalse(viewModel.listMatchesState.value is ListMatchesState.Idle)
        assertFalse(viewModel.listMatchesState.value is ListMatchesState.Start)
    }

    @Test
    fun `cache mode will be enabled`() = runTest {
        viewModel.userIntent.send(ListMatchesIntent.EnableCacheMode)

        assertTrue(viewModel.pagingCacheMode)
    }

    @Test
    fun `layout manager should switch`() = runTest {
        val pre = viewModel.layoutManagerState.value

        viewModel.userIntent.send(ListMatchesIntent.ToggleLayoutManagerState{})

        assertNotEquals(pre, viewModel.layoutManagerState.value)
    }
}