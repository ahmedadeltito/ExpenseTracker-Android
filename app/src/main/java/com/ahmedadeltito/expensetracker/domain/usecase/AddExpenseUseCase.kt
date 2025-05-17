package com.ahmedadeltito.expensetracker.domain.usecase

import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository
import java.util.Date
import java.util.UUID

/**
 * Use case for adding a new expense.
 *
 * This use case encapsulates the business logic required to add an expense.
 * It interacts with the ExpenseRepository to persist the expense data.
 */
class AddExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    /**
     * Executes the use case to add an expense.
     *
     * @param amount The amount of the expense.
     * @param category The category of the expense.
     * @param description Optional description.
     * @param date The date of the expense.
     * @param currencyCode The currency code.
     * @return Result indicating success or failure of the operation.
     */
    suspend operator fun invoke(
        amount: Double,
        category: String,
        description: String?,
        date: Date,
        currencyCode: String,
    ): Result<String> {
        // 1. Validate input (basic validation can also be in the Expense constructor)
        // More complex business rules can be validated here.
        if (amount <= 0) {
            return Result.failure(IllegalArgumentException("Amount must be positive."))
        }
        if (category.isBlank()) {
            return Result.failure(IllegalArgumentException("Category cannot be blank."))
        }
        if (currencyCode.isBlank() || currencyCode.length != 3) {
            return Result.failure(IllegalArgumentException("Invalid currency code."))
        }

        // 2. Create an Expense object
        val newExpense = Expense(
            id = UUID.randomUUID().toString(),
            amount = amount,
            category = category,
            description = description,
            date = date,
            currencyCode = currencyCode.uppercase(),
            creationTimestamp = System.currentTimeMillis()
        )

        // 3. Call the repository to add the expense
        return expenseRepository.addExpense(newExpense)
    }
}