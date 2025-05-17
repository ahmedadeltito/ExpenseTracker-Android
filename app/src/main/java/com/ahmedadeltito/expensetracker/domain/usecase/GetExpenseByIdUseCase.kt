package com.ahmedadeltito.expensetracker.domain.usecase

import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository

/**
 * Use case for retrieving a specific expense by its ID.
 */
class GetExpenseByIdUseCase(
    private val expenseRepository: ExpenseRepository
) {
    /**
     * Executes the use case.
     * @param expenseId The ID of the expense to retrieve.
     * @return A Result containing the Expense if found, or null if not found, or an error.
     */
    suspend operator fun invoke(expenseId: String): Result<Expense?> {
        if (expenseId.isBlank()) {
            return Result.failure(IllegalArgumentException("Expense ID cannot be blank."))
        }
        return expenseRepository.getExpenseById(expenseId)
    }
}