package com.ahmedadeltito.expensetracker.domain.model

import java.util.Date

/**
 * Represents an expense in the domain layer.
 *
 * @property id A unique identifier for the expense. Can be null if it's a new expense not yet saved.
 * @property amount The monetary value of the expense.
 * @property category The category of the expense (e.g., Food, Transport, Utilities).
 * @property description An optional description of the expense.
 * @property date The date when the expense occurred.
 * @property currencyCode The ISO 4217 currency code (e.g., "USD", "EUR", "GBP").
 */
data class Expense(
    val id: String, // Or Long, depending on how you plan to generate/store it
    val amount: Double,
    val category: String, // Could be an Enum or a separate Category entity later
    val description: String?,
    val date: Date, // java.util.Date. Consider kotlinx-datetime for better date/time handling
    val currencyCode: String, // e.g., "USD", "EUR"
    val creationTimestamp: Long
) {
    // Basic validation (could be more sophisticated, perhaps in a factory or use case)
    init {
        require(amount > 0) { "Expense amount must be positive." }
        require(category.isNotBlank()) { "Expense category cannot be blank." }
        require(currencyCode.isNotBlank() && currencyCode.length == 3) {
            "Currency code must be a valid 3-letter ISO 4217 code."
        }
    }
}