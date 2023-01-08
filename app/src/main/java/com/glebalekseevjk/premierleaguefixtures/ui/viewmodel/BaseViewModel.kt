package com.glebalekseevjk.premierleaguefixtures.ui.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T>(initState: T): ViewModel() {
    // Шина состояний
    private val _state: MediatorLiveData<T> = MediatorLiveData<T>().apply {
        value = initState
    }

    val state: LiveData<T> = _state

    val currentState
        get() = _state.value!!

    fun updateState(update: (currentState: T) -> T) {
        val updatedState: T = update(currentState)
        _state.value = updatedState!!
    }

    fun observeState(owner: LifecycleOwner, onChanged: (newState: T) -> Unit) {
        _state.observe(owner, Observer { onChanged(it!!) })
    }

    protected fun <S> subscribeOnDataSource(
        source: LiveData<S>,
        onChanged: (newValue: S, currentState: T) -> T?
    ) {
        _state.addSource(source) {
            _state.value = onChanged(it, currentState) ?: return@addSource
        }
    }
}