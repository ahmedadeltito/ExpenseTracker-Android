package com.ahmedadeltito.expensetracker.domain.usecase

import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving all expenses.
 */
class GetAllExpensesUseCase(
    private val expenseRepository: ExpenseRepository
) {
    /**
     * Executes the use case.
     * @return A Flow emitting a Result containing the list of expenses.
     */
    operator fun invoke(): Flow<Result<List<Expense>>> {
        return expenseRepository.getAllExpenses()
    }
}