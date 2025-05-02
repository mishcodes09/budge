package com.example.prog3c_budgeapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.prog3c_budgeapp.model.Budget.Budget
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddBudgetActivity : AppCompatActivity() {
    private lateinit var categorySpinner: Spinner
    private lateinit var monthEditText: EditText
    private lateinit var amountEditText: EditText

    private val categories = listOf(
        "Food", "Transport", "Shopping", "Bills", "Entertainment", "Health",
        "Education", "Home", "Travel"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_budget) // Use an activity-specific layout

        categorySpinner = findViewById(R.id.budgetCategorySpinner)
        monthEditText = findViewById(R.id.budgetMonth)
        amountEditText = findViewById(R.id.budgetAmount)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        findViewById<View>(R.id.saveBudgetButton).setOnClickListener {
            saveBudget()
        }
    }

    private fun saveBudget() {
        val category = categorySpinner.selectedItem.toString()
        val month = monthEditText.text.toString().trim()
        val amountStr = amountEditText.text.toString().trim()

        if (month.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val maxGoal = intent.getStringExtra("MAX_GOAL") ?: ""
        val budget = Budget(category = category, month = month, amount = amount, budgetGoal = maxGoal)

        val ref = FirebaseDatabase.getInstance()
            .getReference("budgets")
            .child(userId)
            .child(month)
            .child(category)

        ref.setValue(budget)
            .addOnSuccessListener {
                Toast.makeText(this, "Budget saved successfully", Toast.LENGTH_SHORT).show()
                monthEditText.text.clear()
                amountEditText.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}