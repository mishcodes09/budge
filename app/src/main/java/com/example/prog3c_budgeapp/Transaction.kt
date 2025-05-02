
package vcmsa.projects.prog7313budgetapp

import java.io.Serializable

data class Transaction(
    val storeName: String,
    val date: String,
    val amount: Double,
    val receiptImage: Int
) : Serializable
