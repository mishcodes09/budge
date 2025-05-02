
package com.example.prog3c_budgeapp.model.Budget

import java.io.Serializable

data class Budget(
    val category: String = "",
    val month: String = "",
    val amount: Double = 0.0,
    val budgetGoal: String = ""
): Serializable