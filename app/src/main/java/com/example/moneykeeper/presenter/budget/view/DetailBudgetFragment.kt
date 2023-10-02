package com.example.moneykeeper.presenter.budget.view

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentDetailBudgetBinding
import com.example.moneykeeper.domain.model.Budget
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.presenter.expense.adapter.ExpenseAdapter
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.budget.viewmodel.BudgetViewModel
import com.example.moneykeeper.presenter.viewmodel.CategoryViewModel
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.example.moneykeeper.utils.ResourceUtils.getDrawableResourceId
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

import java.util.Locale
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint
class DetailBudgetFragment : BaseFragment<FragmentDetailBudgetBinding>(){
    private lateinit var budget: Budget
    private var listExpese = ArrayList<Expense>()
    @Inject
    lateinit var adapter: ExpenseAdapter
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val expenseViewModel: ExpenseViewModel by activityViewModels()
    private val budgetViewModel:BudgetViewModel by activityViewModels()
    override fun getLayout(container: ViewGroup?): FragmentDetailBudgetBinding {
        return FragmentDetailBudgetBinding.inflate(layoutInflater)
    }

    override fun initViews() {

        binding.rvExpense.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExpense.adapter = adapter
        budget = data as Budget
        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }
        CoroutineScope(Dispatchers.IO).launch {
            val category = categoryViewModel.getCategoryById(budget.budCategory)

            withContext(Dispatchers.Main) {
                binding.ivCategory.setImageResource(requireContext().getDrawableResourceId(category.cateImage))
                binding.tvCategory.text = category.cateName
            }
        }
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.US)
        val formattedDate = dateFormat.format(budget.budMonth)
        binding.tvMonth.text = formattedDate
        binding.tvMoney.text = budget.budMoney
        binding.tvPbBudget.text = budget.budMoney
        expenseViewModel.calculate(budget.budCategory, dateFormat.format(budget.budMonth)) {
            val progressValue = (it.toFloat() / budget.budMoney.toFloat() * 100).toInt()
            binding.pbBudget.progress = progressValue
            binding.pbBudget.show()
            binding.tvPbPercent.text = String.format("%d%%", progressValue)
            binding.tvPbMoney.text = it.toString()
        }

        expenseViewModel.getExpensesForMonth(formattedDate)
        expenseViewModel.expensesMonthLiveData.observe(viewLifecycleOwner){
            listExpese.clear()
            for(expense in it){
                if(expense.expCategory == budget.budCategory){
                 listExpese.add(expense)
                }
            }
           adapter.submitList(listExpese)
        }
        binding.btnDelete.setOnClickListener {
            handleDelete()
        }
        binding.btnUpdate.setOnClickListener {
            callback.showFragment(this::class.java,AddBudgetFragment::class.java,0,0,budget,false)
        }
    }

    private fun handleDelete() {
        budgetViewModel.deleteBudget(budget)
        notify("Xóa thành công")
        callback.backToPrevious()
    }
    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility = View.INVISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.fAB)?.visibility = View.INVISIBLE
    }



}