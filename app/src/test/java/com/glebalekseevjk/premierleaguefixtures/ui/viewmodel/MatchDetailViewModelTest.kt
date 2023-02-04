package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel


import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Resource
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.MatchDetailState
import com.glebalekseevjk.premierleaguefixtures.utils.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MatchDetailViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    lateinit var viewModel: MatchDetailViewModel

    @MockK
    lateinit var matchInfoUseCase: MatchInfoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = MatchDetailViewModel(
            matchInfoUseCase = matchInfoUseCase
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `state should be set to Error`() = runTest {
        coEvery {
            matchInfoUseCase.getMatch(any())
        } returns Resource.Failure(RuntimeException())

        viewModel.setCurrentMatchInfo(1)

        assertTrue(viewModel.state.value is MatchDetailState.Error<*>)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `state should be set to Loaded`() = runTest {
        coEvery {
            matchInfoUseCase.getMatch(any())
        } returns Resource.Success(MatchInfo.MOCK)

        viewModel.setCurrentMatchInfo(1)

        assertTrue(viewModel.state.value is MatchDetailState.Loaded)
    }
}
