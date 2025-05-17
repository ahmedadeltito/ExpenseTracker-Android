package com.ahmedadeltito.expensetracker.data.mapper

import com.ahmedadeltito.expensetracker.data.model.ExpenseCacheModel
import com.ahmedadeltito.expensetracker.domain.model.Expense

// region --- To Domain Model ---
fun ExpenseCacheModel.toDomain(): Expense = with(this) {
    Expense(
        id = id,
        amount = amount,
        category = category,
        description = description,
        date = date,
        currencyCode = currencyCode,
        creationTimestamp = creationTimestamp,
    )
}

fun List<ExpenseCacheModel>.toDomain(): List<Expense> = this.map { it.toDomain() }
// endregion

// region --- To Cache Model ---
fun Expense.toCacheModel(): ExpenseCacheModel = with(this) {
    ExpenseCacheModel(
        id = id,
        amount = amount,
        category = category,
        description = description,
        date = date,
        currencyCode = currencyCode,
        creationTimestamp = creationTimestamp,
        lastUpdatedTimestamp = System.currentTimeMillis()
    )
}
// endregion