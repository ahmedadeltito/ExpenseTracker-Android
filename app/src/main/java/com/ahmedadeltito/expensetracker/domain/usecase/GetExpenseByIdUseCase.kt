package com.ahmedadeltito.expensetracker.domain.usecase

import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

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
    operator fun invoke(expenseId: String): Flow<Result<Expense?>> =
        expenseRepository.getExpenseById(expenseId)
}