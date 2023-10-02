package com.example.moneykeeper.presenter

import android.icu.util.Calendar
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentHomeBinding
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.expense.adapter.ExpenseAdapter
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.example.moneykeeper.presenter.wallet.viewmodel.WalletViewModel
import com.example.moneykeeper.presenter.wallet.adapter.WalletAdapter
import com.example.moneykeeper.presenter.wallet.view.WalletFragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class FragmentHome : BaseFragment<FragmentHomeBinding>() {

    private val expenseViewModel: ExpenseViewModel by activityViewModels()
    private val walletViewModel: WalletViewModel by activityViewModels()


    @Inject
    lateinit var walletAdapter: WalletAdapter
    @Inject
    lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var listExpense: List<Expense>
    override fun getLayout(container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        listExpense = emptyList()

        val calendar = Calendar.getInstance()
        expenseViewModel.getExpensesForYear(calendar.get(Calendar.YEAR).toString())
        expenseViewModel.expensesYearLiveData.observe(this){
            listExpense = it
            handleLineChart()
        }
        handleExpenseRecyclerView()
        handleWalletRecyclerView()





    }

    private fun handleWalletRecyclerView(){
        binding.rvWallet.layoutManager =LinearLayoutManager(requireContext())
        binding.rvWallet.adapter = walletAdapter
        walletViewModel.getWallets()
        walletViewModel.walletsLiveData.observe(this){
            walletAdapter.submitList(it)
        }
        walletAdapter.setItemClickListener(object : OnItemClickListener{
            override fun onItemClick(data: Any?) {
                callback.showFragment(this::class.java, WalletFragment::class.java, 0,0,null,true)
            }

        })



    }

    private fun handleExpenseRecyclerView(){
        binding.rvExpense5.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExpense5.adapter = expenseAdapter
        expenseViewModel.get5Expense()
        expenseViewModel.expense5LiveData.observe(this){

            expenseAdapter.submitList(it)
        }


    }




    private fun handleLineChart(){
        val expenseValue = ArrayList<Entry>()
        val revenueValue = ArrayList<Entry>()





        listExpense.filter { it.expType == 2 }.groupBy { it.expDate to it.expType }.forEach { (key, expenses) ->
            val (date, type) = key
            val totalExpense = expenses.sumBy { it.expMoney.toInt() }
            expenseValue.add(Entry(date.time.toFloat(), totalExpense.toFloat()))
        }


        listExpense.filter { it.expType == 1 }.groupBy { it.expDate to it.expType }.forEach { (key, expenses) ->
            val (date, type) = key
            val totalRevenue = expenses.sumBy { it.expMoney.toInt() }
            revenueValue.add(Entry(date.time.toFloat(), totalRevenue.toFloat()))
        }


        val expense = LineDataSet(expenseValue, "Chi tiêu")
        expense.color = resources.getColor(R.color.button_cancel)
        val revenue = LineDataSet(revenueValue, "Thu nhập")
        revenue.color = resources.getColor(R.color.button_success)
        binding.lcStatistical.animateX(200)
        binding.lcStatistical.axisLeft.setAxisMinValue(0f)
        binding.lcStatistical.description.isEnabled = false
        binding.lcStatistical.axisRight.isEnabled = false
        binding.lcStatistical.xAxis.setDrawGridLines(false)
        binding.lcStatistical.xAxis.setDrawLabels(true)
        binding.lcStatistical.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lcStatistical.xAxis.valueFormatter = object : ValueFormatter() {
            private val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
            private var previousMonth = ""

            override fun getFormattedValue(value: Float): String {
                val date = Date(value.toLong())
                val month = dateFormat.format(date)


                return if (month != previousMonth) {
                    previousMonth = month
                    month
                } else {
                    ""
                }
            }
        }
        val lineData = LineData()
        lineData.addDataSet(expense)
        lineData.addDataSet(revenue)
        binding.lcStatistical.data = lineData
        binding.lcStatistical.invalidate()
    }




}