package com.example.moneykeeper.presenter.expense.view

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentExpensesBinding
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.presenter.expense.adapter.ExpenseAdapter
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.category.viewmodel.CategoryViewModel
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.example.moneykeeper.presenter.utils.NumberFormatter
import com.example.moneykeeper.presenter.utils.ResourceUtils.getDrawableResourceId
import com.example.moneykeeper.presenter.wallet.viewmodel.WalletViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ExpensesFragment : BaseFragment<FragmentExpensesBinding>() {
    private val expenseViewModel: ExpenseViewModel by activityViewModels()

    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val walletViewModel: WalletViewModel by activityViewModels()

    @Inject
    lateinit var expenseAdapter: ExpenseAdapter
    private var currentMonth: Int = 0
    private var currentYear: Int = 0
    private var currentDay: Int = 0
    private var totalExpense: Long = 0
    private var totalRevenue: Long = 0

    private lateinit var listExpense: List<Expense>
    private lateinit var listCategory: List<Category>
    private lateinit var revenueValue: ArrayList<BarEntry>
    private lateinit var revenueLabel: ArrayList<String>
    private lateinit var expenseValue: ArrayList<BarEntry>
    private lateinit var expenseLabel: ArrayList<String>

    override fun getLayout(container: ViewGroup?): FragmentExpensesBinding = FragmentExpensesBinding.inflate(layoutInflater)


    override fun initViews() {
        listExpense = emptyList()
        listCategory = emptyList()
        handleDatePicker()
        categoryViewModel.getCategories()
        categoryViewModel.categoriesLiveData.observe(viewLifecycleOwner){
            listCategory = it
            handleChart()
        }
        handleFilter()
        handleOnClick()
        binding.rvExpense.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExpense.adapter = expenseAdapter
        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }

    }


    private fun handleOnClick(){
        expenseAdapter.setItemClickListener(object : com.example.moneykeeper.presenter.interfaces.OnItemClickListener {
            override fun onItemClick(data: Any?) {
                showBottomSheetDeleteUpdate(data as  Expense)
            }

        })

    }
    private fun showBottomSheetDeleteUpdate(expense: Expense){
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottomsheet_menu_delete_update)
        bottomSheetDialog.show()
        val btnDelete = bottomSheetDialog.findViewById<Button>(R.id.btnDelete)
        btnDelete!!.setOnClickListener {
            expenseViewModel.deleteExpense(expense)
            if(expense.expType == 2){
                CoroutineScope(Dispatchers.IO).launch {
                    val wallet = walletViewModel.getWalletById(expense.expWallet)
                    wallet.walMoney = (wallet.walMoney.toLong() + expense.expMoney.toLong()).toString()
                    walletViewModel.updateWallet(wallet)

                }
            }
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    val wallet = walletViewModel.getWalletById(expense.expWallet)
                    wallet.walMoney = (wallet.walMoney.toLong() - expense.expMoney.toLong()).toString()
                    walletViewModel.updateWallet(wallet)

                }
            }

            bottomSheetDialog.dismiss()
            notify("Xóa thành công")
        }
        val btnUpdate = bottomSheetDialog.findViewById<Button>(R.id.btnUpdate)
        btnUpdate!!.setOnClickListener {
            callback.showFragment(this::class.java, AddExpenseFragment::class.java,0,0,expense,true)

            bottomSheetDialog.dismiss()
        }
    }


    private fun handleMonthPicker(){
        val calendar = Calendar.getInstance()
        currentMonth = calendar.get(Calendar.MONTH)
        currentYear = calendar.get(Calendar.YEAR)
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



        expenseViewModel.getExpensesForMonth(monthYear)
        expenseViewModel.expensesMonthLiveData.observe(this){
            expenseAdapter.submitList(it)
            calculateTotal(it)
            listExpense = it
            handleChart()
            if(it.isEmpty()){
                binding.rvExpense.visibility = View.GONE
                binding.tvEmptyExpense.root.visibility = View.VISIBLE
            }
            else {
                binding.rvExpense.visibility = View.VISIBLE
                binding.tvEmptyExpense.root.visibility = View.GONE
            }
        }

    }


    private fun handleYearPicker() {
        val calendar = Calendar.getInstance()
        currentYear = calendar.get(Calendar.YEAR)
        updateYearText()

        binding.btnPrev.setOnClickListener {
            currentYear--
            updateYearText()
        }

        binding.btnNext.setOnClickListener {
            currentYear++
            updateYearText()
        }
    }

    private fun updateYearText() {
        binding.tvMonth.text = currentYear.toString()
        expenseViewModel.getExpensesForYear(currentYear.toString())
        expenseViewModel.expensesYearLiveData.observe(this){
            expenseAdapter.submitList(it)
            if(it.isEmpty()){
                binding.rvExpense.visibility = View.GONE
                binding.tvEmptyExpense.root.visibility = View.VISIBLE
            }
            else {
                binding.rvExpense.visibility = View.VISIBLE
                binding.tvEmptyExpense.root.visibility = View.GONE
            }
            calculateTotal(it)
            listExpense = it
            handleChart()
        }


    }


    private fun handleDatePicker() {
        val calendar = Calendar.getInstance()
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        currentMonth = calendar.get(Calendar.MONTH)
        currentYear = calendar.get(Calendar.YEAR)

        updateDateText()

        binding.btnPrev.setOnClickListener {
            val tempCalendar = Calendar.getInstance()
            tempCalendar.set(currentYear, currentMonth, currentDay)
            tempCalendar.add(Calendar.DAY_OF_MONTH, -1)

            currentDay = tempCalendar.get(Calendar.DAY_OF_MONTH)
            currentMonth = tempCalendar.get(Calendar.MONTH)
            currentYear = tempCalendar.get(Calendar.YEAR)

            updateDateText()
        }

        binding.btnNext.setOnClickListener {
            val tempCalendar = Calendar.getInstance()
            tempCalendar.set(currentYear, currentMonth, currentDay)
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1)

            currentDay = tempCalendar.get(Calendar.DAY_OF_MONTH)
            currentMonth = tempCalendar.get(Calendar.MONTH)
            currentYear = tempCalendar.get(Calendar.YEAR)

            updateDateText()
        }
    }

    private fun updateDateText() {
        val formattedDate = String.format("%02d/%02d/%04d", currentDay, currentMonth + 1, currentYear)
        binding.tvMonth.text = formattedDate
        expenseViewModel.getExpensesForDay(formattedDate)
        expenseViewModel.expensesDayLiveData.observe(this){
//            var totalE = 0
//            var totalR = 0
//            for(expense in it){
//                if(expense.expType == 2){
//                    totalE ++
//                }
//                else {
//                    totalR ++
//                }
//            }
//            notify(totalR.toString())
//            if(totalE < 1){
//                binding.llBarChartChiTieu.visibility = View.GONE
//            } else if(totalR < 1){
//                binding.llBarChartRvenue.visibility = View.GONE
//            }
//            else {
//                binding.llBarChartRvenue.visibility = View.VISIBLE
//                binding.llBarChartChiTieu.visibility = View.VISIBLE
//            }

            if(it.isEmpty()){
                binding.rvExpense.visibility = View.GONE
                binding.tvEmptyExpense.root.visibility = View.VISIBLE
            }
            else {
                binding.rvExpense.visibility = View.VISIBLE
                binding.tvEmptyExpense.root.visibility = View.GONE
            }
            expenseAdapter.submitList(it)
            calculateTotal(it)
            listExpense = it
            handleChart()
        }

    }
    private fun calculateTotal(expenses: List<Expense>) {
        totalExpense = 0
        totalRevenue = 0

        for (expense in expenses) {
            if (expense.expType == 1) {
                totalRevenue += expense.expMoney.toLong()
            } else {
                totalExpense += expense.expMoney.toLong()
            }
        }

        binding.tvTotalExpense.text = NumberFormatter.formatNumber(totalExpense.toString())
        binding.tvTotalRevenue.text = NumberFormatter.formatNumber(totalRevenue.toString())
        binding.tvTotal.text = (totalRevenue - totalExpense).toString()
    }


    private fun handleChart(){
        revenueValue = ArrayList()
        revenueLabel = ArrayList()
        expenseValue = ArrayList()
        expenseLabel = ArrayList()
        expenseChart()

    }
    private fun expenseChart(){
        revenueValue.clear()
        revenueLabel.clear()
        expenseValue.clear()
        expenseLabel.clear()
        var revenueIndex = 0
        var expenseIndex = 0

        for(category in listCategory){
            var sumExpense: Long = 0
            var sumRevenue: Long = 0
            for(expense in listExpense ){
                if(category.cateId == expense.expCategory && expense.expType == 2){
                    sumExpense += expense.expMoney.toLong()
                }
                if(category.cateId == expense.expCategory && expense.expType == 1){
                    sumRevenue += expense.expMoney.toLong()
                }
            }

            if(sumExpense > 0){
                expenseValue.add(BarEntry(expenseIndex.toFloat(), sumExpense.toFloat()))
                expenseLabel.add(category.cateName)
                expenseIndex++
            }
            if(sumRevenue > 0){
                revenueValue.add(BarEntry(revenueIndex.toFloat(),sumRevenue.toFloat()))
                revenueLabel.add(category.cateName)
                revenueIndex++

            }

        }





        val barDataSetRevenue = BarDataSet(revenueValue, "null")
        val barDataSetExpense = BarDataSet(expenseValue, "null")
        barDataSetRevenue.setColors(*ColorTemplate.MATERIAL_COLORS)
        barDataSetExpense.setColors(*ColorTemplate.MATERIAL_COLORS)
        barDataSetRevenue.valueTextColor = Color.BLACK
        barDataSetExpense.valueTextColor = Color.BLACK
        binding.bcRevenue.description.isEnabled = false
        binding.bcExpense.description.isEnabled = false
        binding.bcRevenue.axisRight.isEnabled = false
        binding.bcExpense.axisRight.isEnabled = false
        binding.bcRevenue.axisLeft.isEnabled = false
        binding.bcExpense.axisLeft.isEnabled = false
        binding.bcRevenue.axisLeft.setAxisMinValue(0f)
        binding.bcExpense.axisLeft.setAxisMinValue(0f)
        binding.bcRevenue.legend.isEnabled = false
        binding.bcExpense.legend.isEnabled = false
        binding.bcRevenue.xAxis.setDrawGridLines(false)
        binding.bcExpense.xAxis.setDrawGridLines(false)
        binding.bcRevenue.animateY(500)
        binding.bcExpense.animateY(500)
        val xAxisReve = binding.bcRevenue.xAxis
        val xAxisExpe = binding.bcExpense.xAxis
        xAxisReve.position = XAxis.XAxisPosition.BOTTOM
        xAxisExpe.position = XAxis.XAxisPosition.BOTTOM
        xAxisReve.textColor = Color.BLACK
        xAxisExpe.textColor = Color.BLACK
        val reveFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < revenueLabel.size) {
                    revenueLabel[index]
                }
                else {
                    ""
                }
            }
        }
        val expeFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < expenseLabel.size) {
                    expenseLabel[index]
                }
                else {
                    ""
                }
            }
        }

        xAxisReve.granularity = 1f
        xAxisExpe.granularity = 1f
        xAxisReve.valueFormatter = reveFormatter
        xAxisExpe.valueFormatter = expeFormatter
        binding.bcRevenue.setDrawGridBackground(false)
        binding.bcExpense.setDrawGridBackground(false)
        binding.bcRevenue.setDrawBarShadow(false)
        binding.bcExpense.setDrawBarShadow(false)

        val barDataRevenue = BarData(barDataSetRevenue)
        val barDataExpense = BarData(barDataSetExpense)
        barDataRevenue.barWidth = 1f
        barDataExpense.barWidth = 1f
        binding.bcRevenue.data = barDataRevenue
        binding.bcExpense.data = barDataExpense
        binding.bcRevenue.invalidate()
        binding.bcExpense.invalidate()
    }





//    private fun expenseChart(){
//        var revenueIndex = 0
//        var expenseIndex = 0
//
//        for(category in listCategory){
//            var sumExpense: Long = 0
//            var sumRevenue: Long = 0
//            for(expense in listExpense ){
//                if(category.cateId == expense.expCategory && expense.expType == 2){
//                    sumExpense += expense.expMoney.toLong()
//                }
//                if(category.cateId == expense.expCategory && expense.expType == 1){
//                    sumRevenue += expense.expMoney.toLong()
//                }
//            }
//
//            if(sumExpense > 0){
//                expenseValue.add(BarEntry(expenseIndex.toFloat(), sumExpense.toFloat()))
//                expenseLabel.add(category.cateName)
//                expenseIndex++
//            }
//            if(sumRevenue > 0){
//                revenueValue.add(BarEntry(revenueIndex.toFloat(),sumRevenue.toFloat()))
//                revenueLabel.add(category.cateName)
//                revenueIndex++
//
//            }
//
//        }
//        val barDataSetRevenue = BarDataSet(revenueValue, "null")
//        val barDataSetExpense = BarDataSet(expenseValue, "null")
//        barDataSetRevenue.setColors(*ColorTemplate.MATERIAL_COLORS)
//        barDataSetExpense.setColors(*ColorTemplate.MATERIAL_COLORS)
//        barDataSetRevenue.valueTextColor = Color.BLACK
//        barDataSetExpense.valueTextColor = Color.BLACK
//        binding.bcRevenue.description.isEnabled = false
//        binding.bcExpense.description.isEnabled = false
//        binding.bcRevenue.axisRight.isEnabled = false
//        binding.bcExpense.axisRight.isEnabled = false
//        binding.bcRevenue.axisLeft.isEnabled = false
//        binding.bcExpense.axisLeft.isEnabled = false
//        binding.bcRevenue.axisLeft.setAxisMinValue(0f)
//        binding.bcExpense.axisLeft.setAxisMinValue(0f)
//        binding.bcRevenue.legend.isEnabled = false
//        binding.bcExpense.legend.isEnabled = false
//        binding.bcRevenue.xAxis.setDrawGridLines(false)
//        binding.bcExpense.xAxis.setDrawGridLines(false)
//        binding.bcRevenue.animateY(500)
//        binding.bcExpense.animateY(500)
//        val xAxisReve = binding.bcRevenue.xAxis
//        val xAxisExpe = binding.bcExpense.xAxis
//        xAxisReve.position = XAxis.XAxisPosition.BOTTOM
//        xAxisExpe.position = XAxis.XAxisPosition.BOTTOM
//        xAxisReve.textColor = Color.BLACK
//        xAxisExpe.textColor = Color.BLACK
//        val formatter: ValueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                val index = value.toInt()
//                return if (index >= 0 && index < revenueLabel.size) {
//                    revenueLabel[index]
//                } else if(index >= 0 && index < expenseLabel.size){
//                    expenseLabel[index]
//                }
//                else {
//                    ""
//                }
//            }
//        }
//
//        xAxisReve.granularity = 1f
//        xAxisExpe.granularity = 1f
//        xAxisReve.valueFormatter = formatter
//        xAxisExpe.valueFormatter = formatter
//        binding.bcRevenue.setDrawGridBackground(false)
//        binding.bcExpense.setDrawGridBackground(false)
//        binding.bcRevenue.setDrawBarShadow(false)
//        binding.bcExpense.setDrawBarShadow(false)
//
//        val barDataRevenue = BarData(barDataSetRevenue)
//        val barDataExpense = BarData(barDataSetExpense)
//        barDataRevenue.barWidth = 1f
//        barDataExpense.barWidth = 1f
//        binding.bcRevenue.data = barDataRevenue
//        binding.bcExpense.data = barDataExpense
//        binding.bcRevenue.invalidate()
//        binding.bcExpense.invalidate()

//        if(expenseValue.isEmpty()){
//            binding.bcExpense.visibility = View.GONE
//            binding.tvEmptyEpenseChart.root.visibility = View.VISIBLE
//        }
//        else {
//            binding.bcExpense.visibility = View.VISIBLE
//            binding.tvEmptyExpense.root.visibility = View.GONE
//        }
//        if(revenueValue.isEmpty()){
//            binding.bcRevenue.visibility = View.GONE
//            binding.tvEmptyRevenueChart.root.visibility = View.VISIBLE
//        }
//        else {
//            binding.bcRevenue.visibility = View.VISIBLE
//            binding.tvEmptyRevenueChart.root.visibility = View.GONE
//        }
//    }

    private fun handleFilter(){
        binding.ivFilter.setOnClickListener{

            val popupMenu = PopupMenu(requireContext(), binding.ivFilter)
            popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu);

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.all -> {
                        binding.tvTitle.text = getString(R.string.all_tran)
                        binding.tvExpenseChart.text = getString(R.string.expe_all_chart)
                        binding.tvRevenueChart.text = getString(R.string.reve_all_chart)
                        expenseViewModel.getExpenses()
                        expenseViewModel.expensesLiveData.observe(viewLifecycleOwner) {
                            expenseAdapter.submitList(it)
                            binding.lnMonth.isVisible = false
                            listExpense = it
                            calculateTotal(it)
                            handleChart()
                        }
                        return@setOnMenuItemClickListener true
                    }
                    R.id.day -> {
                        binding.tvTitle.text = getString(R.string.trans_day)
                        binding.tvExpenseChart.text = getString(R.string.expe_day_chart)
                        binding.tvRevenueChart.text = getString(R.string.reve_day_chart)
                        binding.lnMonth.isVisible = true
                        handleDatePicker()

                        return@setOnMenuItemClickListener true
                    }
                    R.id.year -> {
                        binding.lnMonth.isVisible = true
                        binding.tvTitle.text = getString(R.string.trans_year)
                        binding.tvExpenseChart.text = getString(R.string.expe_year_chart)
                        binding.tvRevenueChart.text = getString(R.string.reve_year_chart)
                        handleYearPicker()
                        return@setOnMenuItemClickListener true

                    }
                    R.id.month -> {
                        binding.lnMonth.isVisible = true
                        binding.tvTitle.text = getString(R.string.trans_month)
                        binding.tvExpenseChart.text = getString(R.string.expe_month_chart)
                        binding.tvRevenueChart.text = getString(R.string.reve_month_chart)
                        handleMonthPicker()
                        return@setOnMenuItemClickListener  true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

}


