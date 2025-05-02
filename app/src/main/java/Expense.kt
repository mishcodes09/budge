package com.example.prog3c_budgeapp.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Expense(
    @get:Exclude
    var id: Long = 0,

    var category: String = "",
    var description: String = "",
    var amount: Double = 0.0,
    var date: String = "",
    var time: String = "",
    var receiptUri: String? = null,

    var userId: String = "",



    @get:Exclude
    var key: String? = null
): Serializable {
    constructor() : this(0, "", "", 0.0, "", "", null)
}
