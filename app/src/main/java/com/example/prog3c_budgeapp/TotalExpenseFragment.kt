package com.example.prog3c_budgeapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prog3c_budgeapp.model.Expense
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class TotalExpensesFragment : Fragment() {

    private lateinit var tvStartDate: TextView
    private lateinit var tvEndDate: TextView
    private lateinit var btnEnter: MaterialButton
    private lateinit var rvCategories: RecyclerView

    private var startDateMillis: Long = 0
    private var endDateMillis: Long = 0

    private lateinit var databaseRef: DatabaseReference

    private val TAG = "TotalExpensesFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_total_expenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvStartDate = view.findViewById(R.id.tvStartDate)
        tvEndDate = view.findViewById(R.id.tvEndDate)
        btnEnter = view.findViewById(R.id.btnEnter)
        rvCategories = view.findViewById(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(requireContext())

        databaseRef = FirebaseDatabase.getInstance().getReference("expenses")

        tvStartDate.setOnClickListener {
            pickDate { dateMillis ->
                startDateMillis = dateMillis
                tvStartDate.text = formatDate(dateMillis)
            }
        }

        tvEndDate.setOnClickListener {
            pickDate { dateMillis ->
                endDateMillis = dateMillis
                tvEndDate.text = formatDate(dateMillis)
            }
        }

        btnEnter.setOnClickListener {
            if (startDateMillis > 0 && endDateMillis > 0) {
                if (startDateMillis > endDateMillis) {
                    Toast.makeText(requireContext(), "Start date must be before end date", Toast.LENGTH_SHORT).show()
                } else {
                    fetchExpenses(startDateMillis, endDateMillis)
                }
            } else {
                Toast.makeText(requireContext(), "Please select both dates", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickDate(onDateSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth, 0, 0, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                onDateSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun formatDate(millis: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    private fun fetchExpenses(startMillis: Long, endMillis: Long) {
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid ?: return

        // Log date range
        Log.d(TAG, "Fetching expenses for user $currentUserId from ${formatDate(startMillis)} to ${formatDate(endMillis)}")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenses = mutableListOf<Expense>()
                val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(Expense::class.java)
                    if (expense != null &&
                        expense.userId == currentUserId &&
                        expense.date.isNotBlank()
                    ) {
                        try {
                            val expenseDateMillis = sdf.parse(expense.date)?.time
                            if (expenseDateMillis != null && expenseDateMillis in startMillis..endMillis) {
                                expenses.add(expense)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Date parse error for ${expense.date}: ${e.message}")
                        }
                    }
                }

                if (expenses.isEmpty()) {
                    Toast.makeText(requireContext(), "No expenses found for the selected date range.", Toast.LENGTH_SHORT).show()
                    rvCategories.adapter = null
                } else {
                    val grouped = expenses.groupBy { it.category }
                        .mapValues { entry -> entry.value.sumOf { it.amount } }

                    val categoryList = grouped.toList() // List<Pair<String, Double>>
                    rvCategories.adapter = CategoryAdapter(categoryList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load expenses: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
