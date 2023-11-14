package com.example.moneykeeper.presenter.expense.view

import android.app.DatePickerDialog
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentAddExpenseBinding
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.presenter.category.view.ChooseCategoryFragment
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.interfaces.ChooseCategoryListener
import com.example.moneykeeper.presenter.interfaces.ChooseWalletListener
import com.example.moneykeeper.presenter.category.viewmodel.CategoryViewModel
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.example.moneykeeper.presenter.wallet.view.ChooseWalletFragment
import com.example.moneykeeper.presenter.wallet.viewmodel.WalletViewModel
import com.example.moneykeeper.presenter.utils.DateUtils
import com.example.moneykeeper.presenter.utils.NumberFormatter
import com.example.moneykeeper.presenter.utils.ResourceUtils.getDrawableResourceId
import com.example.moneykeeper.presenter.utils.TextChanged
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class AddExpenseFragment : BaseFragment<FragmentAddExpenseBinding>() {
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val walletViewModel: WalletViewModel by activityViewModels()
    private val expenseViewModel: ExpenseViewModel by activityViewModels()
    private lateinit var category: Category
    private lateinit var expenseDate: Date
    private lateinit var wallet: Wallet
    private var expenseWallet: Int = 0
    private var isEdit = false
    private var expenseCategory: Int = 0
    private var expenseType: Int = 0

    //
    private lateinit var expenseNote: String
    private lateinit var expenseMoney: String





    override fun getLayout(container: ViewGroup?): FragmentAddExpenseBinding
    = FragmentAddExpenseBinding.inflate(layoutInflater)


    override fun initViews() {
        initInfomation()
        handleButton()
    }

    private fun handleButton() {
        binding.rlCategory.setOnClickListener{
            showChooseCategory(category)
        }
        binding.rlWallet.setOnClickListener{
            showChooseWallet()
        }
        binding.tvExpenseDate.setOnClickListener {
            showDatePicker()
        }
        binding.btnSave.setOnClickListener {
            saveExpense()
        }
        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }
        binding.tvMoney.addTextChangedListener(TextChanged.onTextChangedListener(binding.tvMoney))


    }

    private fun initInfomation(){
        if(data != null && data is Expense ){
            val expense = data as Expense
            isEdit = true

            binding.tvMoney.setText(expense.expMoney)
            CoroutineScope(Dispatchers.IO).launch {
                category = categoryViewModel.getCategoryById(expense.expCategory)
                withContext(Dispatchers.Main) {
                    showCategoryInfomation(category)
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                wallet = walletViewModel.getWalletById(expense.expWallet)
                withContext(Dispatchers.Main) {
                    showWalletInfomation(wallet)
                }
            }

            binding.etExpenseNote.setText(expense.expNote)
            binding.tvExpenseDate.text = DateUtils.formatDate(expense.expDate)
            expenseDate = expense.expDate
        }
        else{
            categoryViewModel.getCategories()

            categoryViewModel.categoriesLiveData.observe(this){
                category = it[7]
                showCategoryInfomation(category)
//                showChooseCategory(category)
            }
            walletViewModel.getWallets()
            walletViewModel.walletsLiveData.observe(this){
                wallet = it[0]
                showWalletInfomation(wallet)
            }
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            binding.tvExpenseDate.text = "${day}/${month + 1}/${year}"
            expenseDate = calendar.time







        }
    }

    private fun saveExpense() {
        expenseNote = binding.etExpenseNote.text.toString()
        expenseMoney = binding.tvMoney.text.toString().replace(",", "")
        if (TextUtils.isEmpty(expenseMoney)) {
            binding.tvMoney.error = getString(R.string.money_blank)
            return
        }
        val expense = Expense(
            expNote = expenseNote,
            expMoney = expenseMoney,
            expDate = expenseDate,
            expType = expenseType,
            expCategory = expenseCategory,
            expWallet = expenseWallet
        )

        if(isEdit){
            val expenseId = (data as Expense).expId
            expense.expId = expenseId
            val money = (data as Expense).expMoney
            expenseViewModel.updateExpense(expense)

            if (expenseType == 2) {
                wallet.walMoney = (wallet.walMoney.toLong() + money.toLong() - expenseMoney.toLong()).toString()
            } else {
                wallet.walMoney = (wallet.walMoney.toLong() - money.toLong() +  expenseMoney.toLong()).toString()
            }
            walletViewModel.updateWallet(wallet)
        }
        else {
            expenseViewModel.insertExpense(expense)
            if(expenseType == 2){
                wallet.walMoney = (wallet.walMoney.toLong() - expenseMoney.toLong()).toString()
            }
            else {
                wallet.walMoney = (wallet.walMoney.toLong() + expenseMoney.toLong()).toString()
            }
            walletViewModel.updateWallet(wallet)
        }
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun showCategoryInfomation(category: Category) {
        binding.ivCategoryImage.setImageResource(requireContext().getDrawableResourceId(category.cateImage))
        binding.tvCategoryName.text = category.cateName
        if (category.cateType == 1) {
            binding.tvCategoryType.text = getString(R.string.revenue)
            binding.tvMoney.setHintTextColor(resources.getColor(R.color.button_success))
            binding.tvMoney.setTextColor(resources.getColor(R.color.button_success))
            binding.tvTitle.text = getString(R.string.add_revenue)
            if (isEdit) {
                binding.tvTitle.text = getString(R.string.update_reve)
            }
        } else {
            binding.tvCategoryType.text = getString(R.string.expense)
            binding.tvMoney.setHintTextColor(resources.getColor(R.color.button_cancel))
//            binding.tvCategoryName.setTextColor(resources.getColor(R.color.button_cancel))
            binding.tvTitle.text = getString(R.string.add_expense)
            if (isEdit) {
                binding.tvTitle.text = getString(R.string.update_expense)
            }
        }
        expenseCategory = category.cateId
        expenseType = category.cateType
    }

    private fun showChooseCategory(category: Category) {
        val fragment: ChooseCategoryFragment = ChooseCategoryFragment.newInstance(category)
        fragment.setDialogFragmentListener(object: ChooseCategoryListener{
            override fun onChooseListener(category: Category) {
                this@AddExpenseFragment.category = category
                showCategoryInfomation(category)
            }

        })


        this.fragmentManager?.let { fragment.show(it,"CHOOSE_CATEGORY") }
    }

    fun showWalletInfomation(wallet: Wallet) {
        binding.ivWallet.setImageResource(requireContext().getDrawableResourceId(wallet.walImage))
        binding.tvWalletName.text = wallet.walName
        binding.tvWalletMoney.text = "Số tiền: " + NumberFormatter.formatNumber(wallet.walMoney)
        expenseWallet = wallet.walId
    }

    private fun showChooseWallet() {
        val fragment: ChooseWalletFragment = ChooseWalletFragment.newInstance()
        fragment.setDialogFragmentListener(object: ChooseWalletListener{
            override fun onChooseListener(wallet: Wallet) {
                this@AddExpenseFragment.wallet = wallet
                showWalletInfomation(wallet)
            }

        })
        this.fragmentManager?.let { fragment.show(it,"CHOOSE_WALLET") }
    }
    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val dateString = "$dayOfMonth/${monthOfYear + 1}/$year"
            binding.tvExpenseDate.text = dateString
            expenseDate = calendar.time
        }, year, month, day)

        datePickerDialog.show()
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clayout)?.visibility = View.INVISIBLE
        activity?.findViewById<AdView>(R.id.adView)?.visibility = View.INVISIBLE

    }




}