package com.example.prog3c_budgeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.prog3c_budgeapp.model.Budget.Budget
import com.example.prog3c_budgeapp.models.Goals
import com.example.prog3c_budgeapp.repository.BudgetRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BudgetGoalFragment : Fragment() {

    private lateinit var minMaxTextView: TextView
    private lateinit var goalContainer: LinearLayout
    private var minGoal = ""
    private var maxGoal = ""
    private val budgetList = BudgetRepository.budgetList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_budget_goals, container, false)

        minMaxTextView = view.findViewById(R.id.minMaxText)
        goalContainer = view.findViewById(R.id.goalContainer)

        view.findViewById<Button>(R.id.setGoalButton).setOnClickListener {
            showSetGoalDialog()
        }

        view.findViewById<Button>(R.id.createBudgetButton).setOnClickListener {
            showCreateBudgetDialog()
        }

        loadSavedGoals()
        displayInMemoryBudgets(inflater)

        return view
    }

    private fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun loadSavedGoals() {
        val userId = getCurrentUserId()
        if (userId == null) {
            showToast("User not logged in.")
            return
        }

        val goalRef = FirebaseDatabase.getInstance().getReference("users/$userId/goals")
        goalRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val goal = snapshot.getValue(Goals::class.java)
                if (goal != null) {
                    minGoal = goal.minGoal
                    maxGoal = goal.maxGoal

                    // Save goals in SharedPreferences for quick access
                    val prefs = requireContext().getSharedPreferences("BudgetPrefs", 0)
                    prefs.edit().putString("minGoal", minGoal)
                        .putString("maxGoal", maxGoal)
                        .apply()

                    minMaxTextView.text = "Min: R$minGoal | Max: R$maxGoal"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Error loading goals: ${error.message}")
            }
        })
    }

    private fun displayInMemoryBudgets(inflater: LayoutInflater) {
        goalContainer.removeAllViews()
        for (budget in budgetList) {
            val itemView = inflater.inflate(R.layout.budget_item, goalContainer, false).apply {
                findViewById<TextView>(R.id.categoryText).text = budget.category
                findViewById<TextView>(R.id.monthText).text = budget.month
                findViewById<TextView>(R.id.amountText2).text = "R%.2f".format(budget.amount)

                setOnClickListener {
                    val fragment = ExpenseReceiptFragment()
                    val bundle = Bundle().apply {
                        putString("category", budget.category)
                        putString("month", budget.month)
                    }
                    fragment.arguments = bundle
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
            goalContainer.addView(itemView)
        }
    }

    private fun showSetGoalDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_set_goals, null)
        val minInput = dialogView.findViewById<EditText>(R.id.minGoalInput)
        val maxInput = dialogView.findViewById<EditText>(R.id.maxGoalInput)

        // Pre-fill the inputs if values are already saved in SharedPreferences
        val prefs = requireContext().getSharedPreferences("BudgetPrefs", 0)
        minInput.setText(prefs.getString("minGoal", ""))
        maxInput.setText(prefs.getString("maxGoal", ""))

        AlertDialog.Builder(requireContext())
            .setTitle("Set Monthly Goal")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val min = minInput.text.toString().trim()
                val max = maxInput.text.toString().trim()
                val minVal = min.toIntOrNull()
                val maxVal = max.toIntOrNull()

                when {
                    min.isEmpty() || max.isEmpty() -> showToast("Enter valid values.")
                    minVal == null || maxVal == null || minVal < 0 || maxVal < 0 -> showToast("Enter positive numbers.")
                    minVal > maxVal -> showToast("Min goal cannot be greater than max goal.")
                    else -> {
                        minGoal = min
                        maxGoal = max
                        minMaxTextView.text = "Min: R$minGoal | Max: R$maxGoal"

                        val goal = Goals(minGoal, maxGoal)
                        val userId = getCurrentUserId()

                        if (userId != null) {
                            // Save the goals in Firebase under the user's record
                            FirebaseDatabase.getInstance().getReference("users")
                                .child(userId)
                                .child("goals")
                                .setValue(goal)
                                .addOnSuccessListener {
                                    requireContext().getSharedPreferences("BudgetPrefs", 0).edit()
                                        .putString("minGoal", minGoal)
                                        .putString("maxGoal", maxGoal)
                                        .apply()
                                    showToast("Goals saved successfully!")
                                }
                                .addOnFailureListener { e ->
                                    showToast("Failed to save goals: ${e.message}")
                                }
                        } else {
                            showToast("User not logged in.")
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showCreateBudgetDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_create_budget, null)
        val categorySpinner = dialogView.findViewById<Spinner>(R.id.budgetCategorySpinner)
        val monthSpinner = dialogView.findViewById<Spinner>(R.id.budgetMonthSpinner)
        val amountInput = dialogView.findViewById<EditText>(R.id.budgetAmount)

        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = monthAdapter

        val categories = mutableListOf<String>()
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoryAdapter

        val dbRef = FirebaseDatabase.getInstance().getReference("expenses")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val uniqueCategories = snapshot.children.mapNotNull {
                    it.child("category").getValue(String::class.java)
                }.toSet()
                categories.clear()
                categories.addAll(uniqueCategories.sorted())
                categoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Error loading categories: ${error.message}")
            }
        })

        AlertDialog.Builder(requireContext())
            .setTitle("Create Budget Category")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val selectedCategory = categorySpinner.selectedItem?.toString()
                val selectedMonth = monthSpinner.selectedItem?.toString()
                val amount = amountInput.text.toString().trim().toDoubleOrNull()

                when {
                    selectedCategory.isNullOrEmpty() -> showToast("Please select a category.")
                    selectedMonth.isNullOrEmpty() -> showToast("Please select a month.")
                    amount == null || amount < 0 -> showToast("Enter a valid positive amount.")
                    else -> {
                        val budget = Budget(
                            category = selectedCategory,
                            month = selectedMonth,
                            amount = amount,
                            budgetGoal = maxGoal
                        )

                        budgetList.add(budget)

                        val userId = getCurrentUserId()
                        if (userId != null) {
                            val budgetRef = FirebaseDatabase.getInstance().getReference("users")
                                .child(userId)
                                .child("budgets")
                                .child(selectedMonth)
                                .child(selectedCategory)

                            budgetRef.setValue(budget)
                                .addOnSuccessListener {
                                    showToast("Budget saved in memory and Firebase!")
                                }
                                .addOnFailureListener { e ->
                                    showToast("Error saving budget: ${e.message}")
                                }
                        } else {
                            showToast("User not logged in.")
                        }

                        displayInMemoryBudgets(layoutInflater)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
