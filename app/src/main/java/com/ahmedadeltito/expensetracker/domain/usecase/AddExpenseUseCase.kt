package com.ahmedadeltito.expensetracker.domain.usecase

import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository

class AddExpenseUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(
        expense: Expense,
    ): Result<String> {
        // 1. Validate input (basic validation can also be in the Expense constructor)
        // More complex business rules can be validated here.
        if (expense.amount <= 0) {
            return Result.failure(IllegalArgumentException("Amount must be positive."))
        }
        if (expense.category.isBlank()) {
            return Result.failure(IllegalArgumentException("Category cannot be blank."))
        }
        if (expense.currencyCode.isBlank() || expense.currencyCode.length != 3) {
            return Result.failure(IllegalArgumentException("Invalid currency code."))
        }

        // 3. Call the repository to add the expense
        return expenseRepository.addExpense(expense)
    }
}