package com.example.moneykeeper.presenter.budget.view

import android.app.AlertDialog
import android.icu.util.Calendar
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentAddBudgetBinding
import com.example.moneykeeper.domain.model.Budget
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.presenter.ChooseCategoryFragment
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.budget.viewmodel.BudgetViewModel
import com.example.moneykeeper.presenter.interfaces.ChooseCategoryListener
import com.example.moneykeeper.utils.ResourceUtils.getDrawableResourceId
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddBudgetFragment : BaseFragment<FragmentAddBudgetBinding>() {
    private lateinit var category: Category
    private val calendar = Calendar.getInstance()
    private var budgetMonth: Date? = null
    private lateinit var formattedDate: String
    private var conflict = false
    private var isEdit = false

    private val budgetViewModel: BudgetViewModel by activityViewModels()
    override fun getLayout(container: ViewGroup?): FragmentAddBudgetBinding {
        return FragmentAddBudgetBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.US)
        if(data != null && data is Budget){
            isEdit = true
        }
        if(isEdit){
            binding.etBudgetMoney.setText((data as Budget).budMoney)
            binding.tvBudgetDate.text = dateFormat.format((data as Budget).budMonth)
            binding.rlBudget.alpha = 0.5f
            binding.rlBudget.isClickable = false
            binding.tvTitle.text = "Cập nhật ngân sách"
        }
        else {
            binding.rlBudget.setOnClickListener {
                val fragment: ChooseCategoryFragment = ChooseCategoryFragment.newInstance()
                fragment.setDialogFragmentListener(object: ChooseCategoryListener{
                    override fun onChooseListener(category: Category) {
                        this@AddBudgetFragment.category = category
                        showCategoryInfomation(category)
                    }

                })
                this.fragmentManager?.let { fragment.show(it,"CHOOSE_CATEGORY") }
            }
        }


        binding.tvBudgetDate.setOnClickListener {
            showMonthYearPickerDialog()
        }

        binding.btnSave.setOnClickListener {
            handleSave()
        }
        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }


        formattedDate = dateFormat.format(calendar.time)
        binding.tvBudgetDate.text = formattedDate

    }
    private fun getDefaultDate(): Date {
        return calendar.time
    }

    private fun handleSave() {
        val money = binding.etBudgetMoney.text.toString().trim()
        if (TextUtils.isEmpty(money)) {
            binding.etBudgetMoney.error = "Số tiền không được để trống!"
            return
        }
        val selectedDate = budgetMonth ?: getDefaultDate()


        if(isEdit){
            val budget = Budget(budId = (data as Budget).budId, budMoney = money, budCategory = (data as Budget).budCategory, budMonth = selectedDate)
            budgetViewModel.updateBudget(budget)
            notify("Cập nhật ngân sách thành công!")
            requireActivity().supportFragmentManager.popBackStack()
        }
        else{
            budgetViewModel.getBudgetForMonthAndYear(formattedDate.toString())
            budgetViewModel.budgetsMonthYearLiveData.observe(viewLifecycleOwner){
                for(budget in it){
                    if(budget.budCategory == if (::category.isInitialized) category.cateId else 1){
                        conflict = true

                    }
                }
            }
            if(conflict){
                notify("Ngân sách đã có, vui lòng sử dụng tính năng cập nhật!")
                return

            }
            val budget = Budget(budMoney = money, budCategory = if (::category.isInitialized) category.cateId else 1,  budMonth = selectedDate )
            budgetViewModel.insertBudget(budget)
            notify("Thêm ngân sách thành công!")
            requireActivity().supportFragmentManager.popBackStack()
        }


    }

    private fun showCategoryInfomation(category: Category) {
        binding.tvCategoryName.text = category.cateName
        binding.ivCategoryImage.setImageResource(requireContext().getDrawableResourceId(category.cateImage))
    }

    private fun showMonthYearPickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        val yearPicker = NumberPicker(requireContext())
        yearPicker.minValue = 1900
        yearPicker.maxValue = 2100
        yearPicker.value = year

        val monthPicker = NumberPicker(requireContext())
        monthPicker.minValue = 0 // January
        monthPicker.maxValue = 11 // December
        monthPicker.displayedValues = DateFormatSymbols().months
        monthPicker.value = month

        val dialogView = LinearLayout(requireContext())
        dialogView.orientation = LinearLayout.HORIZONTAL
        dialogView.addView(yearPicker)
        dialogView.addView(monthPicker)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select Month and Year")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val selectedYear = yearPicker.value
                val selectedMonth = monthPicker.value
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.YEAR, selectedYear)
                selectedCalendar.set(Calendar.MONTH, selectedMonth)
                val selectedDate = selectedCalendar.time

                val formattedDate = String.format("%02d/%04d", selectedMonth + 1, selectedYear)



                binding.tvBudgetDate.text = formattedDate
                budgetMonth = selectedDate
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility = View.INVISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.fAB)?.visibility = View.INVISIBLE
    }


}
