package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.domain.entity.MatchInfo
import com.glebalekseevjk.premierleaguefixtures.domain.entity.ResultType
import com.glebalekseevjk.premierleaguefixtures.domain.interactor.MatchInfoUseCase
import com.glebalekseevjk.premierleaguefixtures.ui.viewmodel.state.MatchDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MatchDetailViewModel @Inject constructor(
    private val matchInfoUseCase: MatchInfoUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<MatchDetailState>(MatchDetailState.Loading)
    val state: StateFlow<MatchDetailState>
        get() = _state

    fun setCurrentMatchInfo(matchNumber: Int) {
        viewModelScope.launch {
            matchInfoUseCase.getMatch(matchNumber).collect {
                when (it) {
                    is ResultType.Failure -> {
                        _state.value = MatchDetailState.Error(R.string.unknown_error_text)
                    }
                    is ResultType.Loading -> {
                        _state.value = MatchDetailState.Loading
                    }
                    is ResultType.Success -> {
                        _state.value = MatchDetailState.Loaded(it.data)
                    }
                }
            }
        }
    }
}