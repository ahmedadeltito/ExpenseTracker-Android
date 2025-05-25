package com.ahmedadeltito.expensetracker.domain.usecase

import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository

/**
 * Use case for deleting an expense by its ID.
 */
class DeleteExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    /**
     * Executes the use case to delete an expense.
     * @param expenseId The ID of the expense to delete.
     * @return Result indicating success or failure of the operation.
     */
    suspend operator fun invoke(expenseId: String): Result<Expense> {
        if (expenseId.isBlank()) {
            return Result.failure(IllegalArgumentException("Expense ID cannot be blank for deletion."))
        }
        return expenseRepository.deleteExpense(expenseId)
    }
}