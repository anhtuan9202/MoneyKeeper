package com.example.moneykeeper.presenter.activity


import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.ActivityMainBinding
import com.example.moneykeeper.presenter.expense.view.AddExpenseFragment
import com.example.moneykeeper.presenter.budget.view.BudgetFragment
import com.example.moneykeeper.presenter.home.FragmentHome
import com.example.moneykeeper.presenter.setting.FragmentSetting
import com.example.moneykeeper.presenter.base.BaseActivity
import com.example.moneykeeper.presenter.expense.view.ExpensesFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun initView() {
        showFragment(this::class.java, FragmentHome::class.java,0,0,null, true )
        binding.botNavView.background = null

        binding.botNavView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHome -> {
                    showFragment(this::class.java, FragmentHome::class.java,0,0,null, true )
                }
                R.id.navExpense -> {
                    showFragment(this::class.java, ExpensesFragment::class.java,0,0,null, true )
                }
                R.id.navSettings -> {
                    showFragment(this::class.java, FragmentSetting::class.java,0,0,null, true )
                }
                R.id.navBudget -> {
                    showFragment(this::class.java, BudgetFragment::class.java,0,0,null, true )

                }
            }
            true
        })

        binding.ivAdd.setOnClickListener {
            showFragment(this::class.java, AddExpenseFragment::class.java,0,0,null, true )
        }

        handleAds()


    }

    private fun handleAds() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }


    override fun getLayout(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)


}