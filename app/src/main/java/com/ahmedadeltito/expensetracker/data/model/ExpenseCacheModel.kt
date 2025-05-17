package com.ahmedadeltito.expensetracker.data.model

import java.util.Date

data class ExpenseCacheModel(
    val id: String,
    val amount: Double,
    val category: String,
    val description: String?,
    val date: Date,
    val currencyCode: String,
    val creationTimestamp: Long,
    val lastUpdatedTimestamp: Long
)