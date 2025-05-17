package com.ahmedadeltito.expensetracker.domain.repository

import com.ahmedadeltito.expensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

/**
 * Interface for accessing expense data.
 * This defines the contract that the data layer must implement.
 */
interface ExpenseRepository {

    /**
     * Adds a new expense.
     * @param expense The expense to add. Note: the input expense might not have an ID yet.
     * The implementation should handle ID generation if necessary.
     * @return The ID of the newly added expense
     */
    suspend fun addExpense(expense: Expense): Result<String>

    /**
     * Retrieves a specific expense by its ID.
     * @param expenseId The ID of the expense to retrieve.
     * @return A Result containing the Expense if found, or an error.
     */
    suspend fun getExpenseById(expenseId: String): Result<Expense?>

    /**
     * Retrieves all expenses as a Flow.
     * The Flow will emit a new list of expenses whenever the underlying data changes.
     * @return A Flow emitting a list of expenses, or an error.
     */
    fun getAllExpenses(): Flow<Result<List<Expense>>>

    /**
     * Updates an existing expense.
     * @param expense The expense with updated details.
     * @return Result indicating success or failure.
     */
    suspend fun updateExpense(expense: Expense): Result<Unit>

    /**
     * Deletes an expense by its ID.
     * @param expenseId The ID of the expense to delete.
     * @return Result indicating success or failure.
     */
    suspend fun deleteExpense(expenseId: String): Result<Unit>

    /**
     * Clears all expenses.
     * @return Result indicating success or failure.
     */
    suspend fun clearAllExpenses(): Result<Unit>
}