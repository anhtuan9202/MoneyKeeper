package com.example.moneykeeper.presenter.budget.view

import android.app.AlertDialog
import android.icu.util.Calendar
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentAddBudgetBinding
import com.example.moneykeeper.domain.model.Budget
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.presenter.category.view.ChooseCategoryFragment
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.budget.viewmodel.BudgetViewModel
import com.example.moneykeeper.presenter.interfaces.ChooseCategoryListener
import com.example.moneykeeper.presenter.utils.ResourceUtils.getDrawableResourceId
import com.example.moneykeeper.presenter.utils.TextChanged
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        binding.etBudgetMoney.addTextChangedListener(TextChanged.onTextChangedListener(binding.etBudgetMoney))

    }
    private fun getDefaultDate(): Date {
        return calendar.time
    }

    private fun handleSave() {
        val money = binding.etBudgetMoney.text.toString().trim().replace(",", "")
        if (TextUtils.isEmpty(money)) {
            binding.etBudgetMoney.error = getString(R.string.blank_money)
            return
        }
        if(!::category.isInitialized){
            notify(getString(R.string.blank_cate))
            return
        }
        val selectedDate = budgetMonth ?: getDefaultDate()


        if(isEdit){
            val budget = Budget(budId = (data as Budget).budId, budMoney = money, budCategory = (data as Budget).budCategory, budMonth = selectedDate)
            budgetViewModel.updateBudget(budget)
            notify(getString(R.string.update_sucess))
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
                notify(getString(R.string.budget_exists))
                return

            }
            val budget = Budget(budMoney = money, budCategory = if (::category.isInitialized) category.cateId else 1,  budMonth = selectedDate )
            budgetViewModel.insertBudget(budget)
            notify(getString(R.string.add_succes))
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
        activity?.findViewById<ConstraintLayout>(R.id.clayout)?.visibility = View.INVISIBLE
        activity?.findViewById<AdView>(R.id.adView)?.visibility = View.INVISIBLE


    }


}
