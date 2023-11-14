package com.example.moneykeeper.presenter.budget.view

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneykeeper.databinding.FragmentBudgetBinding
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.budget.adapter.BudgetAdapter
import com.example.moneykeeper.presenter.budget.viewmodel.BudgetViewModel
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.example.moneykeeper.presenter.utils.NumberFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BudgetFragment : BaseFragment<FragmentBudgetBinding>() {
    private val budgetViewModel: BudgetViewModel by activityViewModels()
    private val expenseViewModel: ExpenseViewModel by activityViewModels()

    private var currentMonth: Int = 0
    private var currentYear: Int = 0
    @Inject
    lateinit var adapter: BudgetAdapter

    override fun getLayout(container: ViewGroup?): FragmentBudgetBinding {
        return FragmentBudgetBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        binding.rvBudget.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBudget.adapter = adapter

        binding.ivAdd.setOnClickListener {
            callback.showFragment(this::class.java, AddBudgetFragment::class.java, 0,0,null,true)
        }
        adapter.setItemClickListener(object:OnItemClickListener{
            override fun onItemClick(data: Any?) {
                callback.showFragment(this::class.java, DetailBudgetFragment::class.java,0,0,data,true)
            }

        })
        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }
        handleMonthPicker()
    }
    private fun handleMonthPicker(){
        val calendar = java.util.Calendar.getInstance()
        currentMonth = calendar.get(java.util.Calendar.MONTH)
        currentYear = calendar.get(java.util.Calendar.YEAR)
        updateMonthYearText()

        binding.btnPrev.setOnClickListener {
            currentMonth --
            if(currentMonth < 0){
                currentMonth = 11
                currentYear --
            }
            updateMonthYearText()
        }

        binding.btnNext.setOnClickListener {
            currentMonth++
            if (currentMonth > 11) {
                currentMonth = 0
                currentYear++
            }
            updateMonthYearText()
        }
    }

    private fun updateMonthYearText() {
        val monthYear = String.format("%02d/%d", currentMonth + 1, currentYear)
        binding.tvMonth.text = monthYear

        budgetViewModel.getBudgetForMonthAndYear(monthYear)
        budgetViewModel.budgetsMonthYearLiveData.observe(viewLifecycleOwner){
            var total = 0
            adapter.submitList(it)
            for(budget in it){
                total += budget.budMoney.toInt()
            }
            binding.tvBudget.text = NumberFormatter.formatNumber(total.toString())
            if(it.isEmpty()){
                binding.rvBudget.visibility = View.GONE
                binding.tvEmptyBudget.root.visibility = View.VISIBLE
            }
            else {
                binding.rvBudget.visibility = View.VISIBLE
                binding.tvEmptyBudget.root.visibility = View.GONE
            }

        }
        expenseViewModel.getExpensesForMonth(monthYear)
        expenseViewModel.expensesMonthLiveData.observe(viewLifecycleOwner){
            var total = 0
            for(expense in it){
                total += expense.expMoney.toInt()
            }
            binding.tvMoney.text = NumberFormatter.formatNumber(total.toString())
        }



    }



}