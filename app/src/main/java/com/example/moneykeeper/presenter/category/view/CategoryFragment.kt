package com.example.moneykeeper.presenter.category.view

import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentCategoryBinding
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.category.adapter.CategoryAdapter
import com.example.moneykeeper.presenter.interfaces.OnDeleteClickListener
import com.example.moneykeeper.presenter.category.viewmodel.CategoryViewModel
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.google.android.material.button.MaterialButtonToggleGroup
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {
    private var categoryCount: Int = 0
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val expenseViewModel: ExpenseViewModel by activityViewModels()
    @Inject
    lateinit var  categoryAdapter: CategoryAdapter
    override fun getLayout(container: ViewGroup?): FragmentCategoryBinding {
        return FragmentCategoryBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }
        binding.rvCategory.layoutManager = GridLayoutManager(context, 4)
        binding.rvCategory.adapter = categoryAdapter
        showRevenue()
        binding.tgbCategory.addOnButtonCheckedListener(MaterialButtonToggleGroup.OnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.btnExpense -> {
                    updateButtonUI(isRevenue = false)
                    showExpense()
                }

                R.id.btnRevenue -> {
                    updateButtonUI(isRevenue = true)
                    showRevenue()
                }
            }
        })
        binding.fab.setOnClickListener {
            callback.showFragment(this::class.java, AddCategoryFragment::class.java,0,0,null)
        }
        binding.btnDelete.setOnClickListener {
            if(categoryAdapter.showTrashIcon){
                categoryAdapter.showTrashIcon(false)
            }else {
                categoryAdapter.showTrashIcon(true)

            }
        }
        categoryAdapter.setDeleteClickListener(object : OnDeleteClickListener{
            override fun onDeleteClick(data: Any?) {
                val category = data as Category
                if(categoryCount > 1){
                    categoryViewModel.deleteCategory(category)
                    expenseViewModel.getExpenses()
                    expenseViewModel.expensesLiveData.observe(viewLifecycleOwner){
                        for(expense in it){
                            if(expense.expCategory == category.cateId){
                                expenseViewModel.deleteExpense(expense)
                            }
                        }
                    }
                    notify(getString(R.string.delete_success))
                }
                else {
                    notify(getString(R.string.category_blank))

                }

            }

        })
    }
    private fun updateButtonUI(isRevenue: Boolean) {
        val revenueColor = if (isRevenue) com.example.moneykeeper.R.color.button_checked else com.example.moneykeeper.R.color.button_uncheck
        val expenseColor = if (!isRevenue) com.example.moneykeeper.R.color.button_checked else com.example.moneykeeper.R.color.button_uncheck

        binding.btnRevenue.setBackgroundColor(resources.getColor(revenueColor))
        binding.btnRevenue.setTextColor(resources.getColor(if (isRevenue) com.example.moneykeeper.R.color.white else com.example.moneykeeper.R.color.black))
        binding.btnExpense.setBackgroundColor(resources.getColor(expenseColor))
        binding.btnExpense.setTextColor(resources.getColor(if (!isRevenue) com.example.moneykeeper.R.color.white else com.example.moneykeeper.R.color.black))
    }

    private fun showRevenue() {
        categoryViewModel.getRevenues()
        categoryViewModel.revenuesLiveData.observe(viewLifecycleOwner) { it ->
            categoryAdapter.submitList(it)
            categoryCount = it.size
        }

    }

    private fun showExpense() {
        categoryViewModel.getExpenses()
        categoryViewModel.expensesLiveData.observe(viewLifecycleOwner) { it ->
            categoryAdapter.submitList(it)
            categoryCount = it.size
        }
    }



}