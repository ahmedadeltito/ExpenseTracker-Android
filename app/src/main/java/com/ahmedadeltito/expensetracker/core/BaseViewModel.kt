package com.ahmedadeltito.expensetracker.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class to encapsulate common UDF logic.
 *
 * @param S The type of the UiState.
 * @param E The type of the UiEvent.
 * @param F The type of the UiSideEffect.
 */
abstract class BaseViewModel<S : UiState, E : UiEvent, F : UiSideEffect> : ViewModel() {

    // Backing property for UiState, initialized with createInitialState()
    private val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(createInitialState()) }
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // Channel for UiSideEffects. Channels are good for one-time events.
    // Alternatively, MutableSharedFlow can be used.
    // Using Channel for single-shot events ensuring each event is consumed at most once by one collector.
    private val _sideEffect: Channel<F> = Channel(Channel.CONFLATED)
    val sideEffect: Flow<F> = _sideEffect.receiveAsFlow()

    /**
     * Must be implemented by subclasses to provide the initial state.
     */
    protected abstract fun createInitialState(): S

    /**
     * Public function for the UI to send events to the ViewModel.
     */
    fun onEvent(event: E) {
        handleEvent(event)
    }

    /**
     * Must be implemented by subclasses to handle specific UiEvents.
     * This is where the logic for processing events and updating state or triggering side effects resides.
     */
    protected abstract fun handleEvent(event: E)

    /**
     * Helper function to update the UiState.
     * It should be called from within viewModelScope or another coroutine.
     */
    protected fun updateUiState(reducer: S.() -> S) {
        _uiState.value = uiState.value.reducer()
    }

    /**
     * Helper function to trigger a UiSideEffect.
     * It launches a new coroutine in viewModelScope to send the effect.
     */
    protected fun onSideEffect(effect: F) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}