package com.example.prog3c_budgeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prog3c_budgeapp.model.Expense

class ExpenseAdapter(
    private var expenses: List<Expense>,
    private val listener: OnExpenseClickListener
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    interface OnExpenseClickListener {
        fun onExpenseClick(expense: Expense)
    }

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onExpenseClick(expenses[position])
                }
            }
        }

        fun bind(expense: Expense) {
            itemView.findViewById<TextView>(R.id.descriptionText).text = expense.description
            itemView.findViewById<TextView>(R.id.amountText).text = "R${expense.amount}"
            itemView.findViewById<TextView>(R.id.dateText).text = expense.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(expenses[position])
    }

    override fun getItemCount(): Int = expenses.size

    fun updateData(newExpenses: List<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}
