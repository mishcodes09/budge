package com.example.prog3c_budgeapp

import ReceiptDetailActivity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prog3c_budgeapp.model.Expense
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.*

class ExpenseReceiptFragment : Fragment(), ExpenseAdapter.OnExpenseClickListener {

    private lateinit var chart: LineChart
    private lateinit var transactionList: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var chartTitle: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expenses, container, false)

        chart = view.findViewById(R.id.lineChart)
        transactionList = view.findViewById(R.id.transactionList)
        chartTitle = view.findViewById(R.id.chartTitle)

        val categoryArg = arguments?.getString("category")
        val monthArg = arguments?.getString("month")

        chartTitle.text = if (categoryArg != null) {
            "Expenses: $categoryArg"
        } else {
            "Expenses"
        }

        database = FirebaseDatabase.getInstance(
            "https://budgeapp-846a9-default-rtdb.asia-southeast1.firebasedatabase.app/"
        ).getReference("expenses")

        transactionList.layoutManager = LinearLayoutManager(requireContext())

        // Pass the listener (this) to the adapter
        val adapter = ExpenseAdapter(mutableListOf(), this)  // 'this' refers to the fragment as the listener
        transactionList.adapter = adapter

        fetchExpensesFromFirebase(categoryArg, monthArg)

        view.findViewById<Button>(R.id.backToBudgetBtn).setOnClickListener {
            parentFragmentManager.popBackStack()  // Navigates back to BudgetGoalFragment
        }

        return view
    }

    private fun fetchExpensesFromFirebase(categoryArg: String?, monthArg: String?) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val entries = mutableListOf<Entry>()
                val expenses = mutableListOf<Expense>()
                var index = 0f

                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(Expense::class.java)
                    if (expense != null) {
                        val matchesCategory = categoryArg == null || expense.category == categoryArg
                        val matchesMonth = monthArg == null || expense.date.contains(monthArg, ignoreCase = true)

                        if (matchesCategory && matchesMonth) {
                            expenses.add(expense)
                            entries.add(Entry(index++, expense.amount.toFloat()))
                        }
                    }
                }

                // Update line chart
                if (entries.isNotEmpty()) {
                    val dataSet = LineDataSet(entries, "Spending")
                    dataSet.setDrawValues(false)
                    dataSet.setDrawCircles(true)
                    chart.data = LineData(dataSet)
                    chart.invalidate()
                } else {
                    chart.clear()
                    chart.invalidate()
                }

                // Update adapter
                (transactionList.adapter as? ExpenseAdapter)?.apply {
                    updateData(expenses)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle DB error
            }
        })
    }

    // Implement the onExpenseClick method from the listener
    override fun onExpenseClick(expense: Expense) {
        // Create an intent to start ReceiptDetailActivity
        val intent = Intent(requireContext(), ReceiptDetailActivity::class.java)
        intent.putExtra("transaction", expense)  // Pass the expense object
        startActivity(intent)
    }
}