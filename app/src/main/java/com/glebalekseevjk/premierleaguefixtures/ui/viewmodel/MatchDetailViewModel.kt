package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.domain.entity.Resource
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
            when (val result = matchInfoUseCase.getMatch(matchNumber)) {
                is Resource.Failure -> _state.value = MatchDetailState.Error(R.string.error_text)
                is Resource.Success -> _state.value = MatchDetailState.Loaded(result.data)
            }
        }
    }
}