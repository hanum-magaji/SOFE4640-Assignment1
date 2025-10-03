package com.example.assignment1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.assignment1.databinding.FragmentFirstBinding
import kotlin.math.pow

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set a click listener on the calculate button
        binding.btnCalculate.setOnClickListener {
            calculateAndDisplayResults()
        }
    }

    /**
     * Reads user inputs, performs calculations, and updates the UI.
     */
    private fun calculateAndDisplayResults() {
        // Read input values and convert to Double, defaulting to 0.0 if empty or invalid
        val loanAmount = binding.etLoanAmount.text.toString().toDoubleOrNull() ?: 0.0
        val annualRate = binding.etInterestRate.text.toString().toDoubleOrNull() ?: 0.0
        val tenureYears = binding.etLoanTenure.text.toString().toIntOrNull() ?: 0
        val monthlyIncome = binding.etMonthlyIncome.text.toString().toDoubleOrNull() ?: 0.0
        val monthlyExpenses = binding.etMonthlyExpenses.text.toString().toDoubleOrNull() ?: 0.0

        // EMI Calculation
        // Basic input validation
        if (loanAmount <= 0 || annualRate <= 0 || tenureYears <= 0) {
            Toast.makeText(context, "Please enter valid loan details.", Toast.LENGTH_SHORT).show()
            return
        }

        val monthlyEmi = calculateEmi(loanAmount, annualRate, tenureYears)
        binding.tvMonthlyEMI.text = String.format("Monthly EMI: $%.2f", monthlyEmi)

        // Budget Balance Calculation
        val totalDeductions = monthlyEmi + monthlyExpenses
        val budgetBalance = monthlyIncome - totalDeductions

        // Display the final balance with appropriate label (Savings/Deficit)
        if (budgetBalance >= 0) {
            binding.tvBudgetBalance.text = String.format("Monthly Savings: $%.2f", budgetBalance)
            
        } else {
            binding.tvBudgetBalance.text = String.format("Monthly Deficit: $%.2f", -budgetBalance)
          
        }
    }

    /**
     * Calculates the Equated Monthly Installment (EMI).
     * @param principal The total loan amount.
     * @param annualRate The annual interest rate in percent.
     * @param tenureYears The loan period in years.
     * @return The calculated monthly EMI as a Double.
     */
    private fun calculateEmi(principal: Double, annualRate: Double, tenureYears: Int): Double {
        // Convert annual rate to monthly rate and tenure to months
        val monthlyRate = annualRate / (12 * 100)
        val tenureMonths = tenureYears * 12

        // EMI formula: P * r * (1+r)^n / ((1+r)^n - 1)
        if (monthlyRate == 0.0) return principal / tenureMonths // Handle zero interest case

        val emi = principal * monthlyRate * (1 + monthlyRate).pow(tenureMonths) /
                ((1 + monthlyRate).pow(tenureMonths) - 1)

        return emi
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
