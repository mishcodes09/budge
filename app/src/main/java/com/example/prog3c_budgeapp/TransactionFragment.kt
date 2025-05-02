package vcmsa.projects.prog7313budgetapp

import ReceiptDetailActivity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prog3c_budgeapp.ExpenseAdapter
import com.example.prog3c_budgeapp.R
import com.example.prog3c_budgeapp.model.Expense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionFragment : Fragment(), ExpenseAdapter.OnExpenseClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private val expenses = mutableListOf<Expense>()

    private lateinit var startDateEditText: EditText
    private lateinit var endDateEditText: EditText
    private lateinit var enterButton: Button

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExpenseAdapter(expenses, this)
        recyclerView.adapter = adapter

        startDateEditText = view.findViewById(R.id.startDateEditText)
        endDateEditText = view.findViewById(R.id.endDateEditText)
        enterButton = view.findViewById(R.id.enterButton)

        startDateEditText.setOnClickListener {
            showDatePickerDialog(startDateEditText)
        }

        endDateEditText.setOnClickListener {
            showDatePickerDialog(endDateEditText)
        }

        enterButton.setOnClickListener {
            filterExpensesByDate()
        }

        loadExpensesFromFirebase()

        return view
    }

    override fun onExpenseClick(expense: Expense) {
        val intent = Intent(requireContext(), ReceiptDetailActivity::class.java)
        intent.putExtra("expense", expense)
        startActivity(intent)
    }

    private fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                editText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun filterExpensesByDate() {
        val currentUserId = getCurrentUserId()
        if (currentUserId == null) {
            Toast.makeText(requireContext(), "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        val startDateStr = startDateEditText.text.toString()
        val endDateStr = endDateEditText.text.toString()

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please select both dates.", Toast.LENGTH_SHORT).show()
            return
        }

        val startDate = try {
            dateFormat.parse(startDateStr)
        } catch (e: Exception) {
            null
        }

        val endDate = try {
            dateFormat.parse(endDateStr)
        } catch (e: Exception) {
            null
        }

        if (startDate == null || endDate == null) {
            Toast.makeText(requireContext(), "Invalid date format.", Toast.LENGTH_SHORT).show()
            return
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("expenses")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                expenses.clear()
                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(Expense::class.java)
                    expense?.let {
                        if (it.userId == currentUserId) {
                            try {
                                val expenseDate = dateFormat.parse(it.date)
                                if (expenseDate != null &&
                                    !expenseDate.before(startDate) &&
                                    !expenseDate.after(endDate)
                                ) {
                                    it.key = expenseSnapshot.key
                                    expenses.add(it)
                                }
                            } catch (e: Exception) {
                                // Invalid date, skip
                            }
                        }
                    }
                }
                adapter.updateData(expenses)
                if (expenses.isEmpty()) {
                    Toast.makeText(requireContext(), "No transactions found in this range.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load expenses.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadExpensesFromFirebase() {
        val currentUserId = getCurrentUserId()
        if (currentUserId == null) {
            Toast.makeText(requireContext(), "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        val dbRef = FirebaseDatabase.getInstance().getReference("expenses")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                expenses.clear()
                for (expenseSnapshot in snapshot.children) {
                    val expense = expenseSnapshot.getValue(Expense::class.java)
                    expense?.let {
                        if (it.userId == currentUserId) {
                            it.key = expenseSnapshot.key
                            expenses.add(it)
                        }
                    }
                }
                adapter.updateData(expenses)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load expenses.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
