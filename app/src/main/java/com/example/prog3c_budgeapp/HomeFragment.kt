package com.example.prog3c_budgeapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.example.prog3c_budgeapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set user name and wallet data
        binding.userGreeting.text = "Hello, Charlie"
        binding.cashAmount.text = "10,000.00"
        binding.expensesAmount.text = "4500.00"
        binding.incomeAmount.text = "15,000.00"

        setupBarChart()


    }


    private fun setupBarChart() {
        val entries = listOf(
            BarEntry(0f, 500f),  // First bar
            BarEntry(1f, 1200f), // Second bar
            BarEntry(2f, 2000f), // Third bar
            BarEntry(3f, 2800f), // Fourth bar
            BarEntry(4f, 3500f)  // Fifth bar
        )

        val barDataSet = BarDataSet(entries, "Expenses")

        // Set the colors for each bar
        barDataSet.colors = listOf(
            Color.parseColor("#5B21B6"), // Purple
            Color.parseColor("#3B82F6"), // Blue
            Color.parseColor("#10B981"), // Green
            Color.parseColor("#84CC16"), // Light green
            Color.parseColor("#FBBF24")  // Yellow
        )

        barDataSet.setDrawValues(false)

        val barData = BarData(barDataSet)
        binding.barChart.data = barData

        // Customize the chart
        binding.barChart.description.isEnabled = false
        binding.barChart.legend.isEnabled = false
        binding.barChart.axisRight.isEnabled = false
        binding.barChart.axisLeft.isEnabled = false
        binding.barChart.xAxis.isEnabled = false
        binding.barChart.setTouchEnabled(false)
        binding.barChart.setDrawGridBackground(false)
        binding.barChart.setDrawBorders(false)

        // Set space between bars
        barData.barWidth = 0.7f

        binding.barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}