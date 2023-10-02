package com.example.moneykeeper.presenter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentChooseCategoryBinding
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.presenter.base.BaseBottomSheetFragment
import com.example.moneykeeper.presenter.interfaces.ChooseCategoryListener
import com.example.moneykeeper.presenter.viewmodel.CategoryViewModel
import com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChooseCategoryFragment : BaseBottomSheetFragment<FragmentChooseCategoryBinding>() {
    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private lateinit var listener: ChooseCategoryListener

    fun setDialogFragmentListener(listener: ChooseCategoryListener) {
        this.listener = listener
    }

    override fun getLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChooseCategoryBinding {
       return FragmentChooseCategoryBinding.inflate(layoutInflater)
    }

    companion object {
        fun newInstance(category: Category): ChooseCategoryFragment {
            val fragment = ChooseCategoryFragment()
            val args = Bundle().apply {
                putInt("id", category.cateId)
                putString("name", category.cateName)
                putString("img", category.cateImage)
                putInt("type", category.cateType)
            }
            fragment.arguments = args
            return fragment
        }
        fun newInstance(): ChooseCategoryFragment {
            return ChooseCategoryFragment()
        }
    }


    override fun initViews() {
        categoryViewModel.getRevenues()
        categoryViewModel.getExpenses()

        binding.tgbCategoryType.addOnButtonCheckedListener(OnButtonCheckedListener { _, checkedId, _ ->
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
        if (arguments != null && requireArguments().getInt("type") == 2) {
            showExpense()

        } else {
            showRevenue()

        }
        binding.rvChooseCategory.layoutManager = GridLayoutManager(context, 4)
        binding.rvChooseCategory.adapter = categoryAdapter
        categoryAdapter.setItemClickListener(object : com.example.moneykeeper.presenter.interfaces.OnItemClickListener {
            override fun onItemClick(data: Any?) {
                if(data == "add"){
                    callback.showFragment(this::class.java,AddCategoryFragment::class.java,0,0,null,true)
                    dismiss()
                }
                else {
                    val category = data as Category
                    listener.onChooseListener(category)
                    dismiss()

                }

            }
        })
    }
    private fun updateButtonUI(isRevenue: Boolean) {
        val revenueColor = if (isRevenue) R.color.button_checked else R.color.button_uncheck
        val expenseColor = if (!isRevenue) R.color.button_checked else R.color.button_uncheck

        binding.btnRevenue.setBackgroundColor(resources.getColor(revenueColor))
        binding.btnRevenue.setTextColor(resources.getColor(if (isRevenue) R.color.white else R.color.black))
        binding.btnExpense.setBackgroundColor(resources.getColor(expenseColor))
        binding.btnExpense.setTextColor(resources.getColor(if (!isRevenue) R.color.white else R.color.black))
    }

    private fun showRevenue() {
        categoryViewModel.revenuesLiveData.observe(viewLifecycleOwner) { it ->
            val data = mutableListOf<Any>()
            it?.let {
                data.addAll(it)
            }
            data.add("add")
            categoryAdapter.submitList(data)
        }
    }

    private fun showExpense() {
        categoryViewModel.expensesLiveData.observe(viewLifecycleOwner) { it ->
            val data = mutableListOf<Any>()
            it?.let {
                data.addAll(it)
            }
            data.add("add")
            categoryAdapter.submitList(data)

        }
    }





}