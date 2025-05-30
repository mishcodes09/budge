package com.example.prog3c_budgeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import com.example.prog3c_budgeapp.databinding.ActivityDashboardBinding
import com.example.prog3c_budgeapp.AddExpenseFragment
import com.example.prog3c_budgeapp.HomeFragment
import com.example.prog3c_budgeapp.model.Budget.Budget
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vcmsa.projects.prog7313budgetapp.TransactionFragment
import android.content.Intent

class Dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load the home fragment by default
        loadFragment(HomeFragment())

        // Find the nav elements inside the container
        val navHome = binding.root.findViewById<LinearLayout>(R.id.navHome)
        val navTransact = binding.root.findViewById<LinearLayout>(R.id.navTransact)
        val fabAdd = binding.root.findViewById<FloatingActionButton>(R.id.fabAdd)
        val navBudget = binding.root.findViewById<LinearLayout>(R.id.navBudget)
        val navExpenses = binding.root.findViewById<LinearLayout>(R.id.navExpenses)


        // Set up navigation click listeners
        navHome.setOnClickListener {
            loadFragment(HomeFragment())
        }

        navTransact.setOnClickListener {
            // Placeholder - replace with actual transaction fragment
            loadFragment(TransactionFragment())
        }

        fabAdd.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }


        navBudget.setOnClickListener {
            // Placeholder - replace with actual budget fragment
            loadFragment(BudgetGoalFragment())
        }

        navExpenses.setOnClickListener {
            // Placeholder - replace with actual expenses fragment
            loadFragment(TotalExpensesFragment())
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        return true
    }
}
