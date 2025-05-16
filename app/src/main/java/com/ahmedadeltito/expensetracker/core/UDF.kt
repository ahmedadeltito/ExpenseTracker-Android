package com.ahmedadeltito.expensetracker.core

/**
 * Represents a state that the UI can observe and render.
 * It should be an immutable data class.
 */
interface UiState

/**
 * Represents an action triggered by the user or the system from the UI.
 */
interface UiEvent

/**
 * Represents a one-time event that the UI should handle,
 * such as navigation, showing a toast, etc.
 */
interface UiSideEffect