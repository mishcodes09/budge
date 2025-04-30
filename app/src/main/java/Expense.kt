package com.example.prog3c_budgeapp.model

data class Expense(
    val id: Long,
    val category: String,
    val description: String,
    val amount: Double,
    val isRecurring: Boolean,
    val date: Long, // Timestamp
    val receiptUri: String? = null
)