package com.ahmedadeltito.expensetracker.domain.usecase

import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository

/**
 * Use case for updating an existing expense.
 */
class UpdateExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    /**
     * Executes the use case to update an expense.
     * The Expense object must contain the ID of the expense to be updated.
     * @param expense The expense object with updated details.
     * @return Result indicating success or failure of the operation.
     */
    suspend operator fun invoke(expense: Expense): Result<Expense> {
        // The Expense data class already has some validation in its init block.
        // We can add more complex business rule validation here if needed before updating.
        // For example, ensuring the ID is not blank.
        if (expense.id.isBlank()) {
            return Result.failure(IllegalArgumentException("Expense ID cannot be blank for an update."))
        }
        return expenseRepository.updateExpense(expense)
    }
}