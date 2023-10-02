package com.example.moneykeeper.presenter.wallet.view

import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentWalletBinding
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.example.moneykeeper.presenter.wallet.viewmodel.WalletViewModel
import com.example.moneykeeper.presenter.wallet.adapter.WalletAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WalletFragment() : BaseFragment<FragmentWalletBinding>() {
    private var walletCount: Int = 0
    private lateinit var wallet: Wallet
    private val walletViewModel: WalletViewModel by activityViewModels()
    private val expenseViewModel: ExpenseViewModel by activityViewModels()
    @Inject
    lateinit var walletAdapter: WalletAdapter
    override fun getLayout(container: ViewGroup?): FragmentWalletBinding {
        return FragmentWalletBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        binding.rvWallet.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWallet.adapter = walletAdapter
        walletViewModel.getWallets()
        walletViewModel.walletsLiveData.observe(this){
            walletAdapter.submitList(it)
            walletCount = it.size
        }
        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }
        binding.ivAdd.setOnClickListener{
            callback.showFragment(this::class.java,AddWalletFragment::class.java,0,0,null,true)
        }

        walletAdapter.setItemClickListener(object: com.example.moneykeeper.presenter.interfaces.OnItemClickListener{
            override fun onItemClick(data: Any?) {
                if(data != null){
                    wallet = data as Wallet
//                    callback.showFragment(this::class.java,AddWalletFragment::class.java,0,0,data,true)
                        showBottomSheetDeleteUpdate(data)
                }

            }

        })
    }


    private fun showBottomSheetDeleteUpdate(wallet: Wallet){
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottomsheet_menu_delete_update)
        bottomSheetDialog.show()
        val btnDelete = bottomSheetDialog.findViewById<Button>(R.id.btnDelete)
        btnDelete!!.setOnClickListener {
            if (walletCount> 1) {
                walletViewModel.deleteWallet(wallet)
                expenseViewModel.getExpenses()
                expenseViewModel.expensesLiveData.observe(this){
                    for(expense in it){
                        if(expense.expWallet == wallet.walId){
                            expenseViewModel.deleteExpense(expense)
                        }
                    }
                }

                bottomSheetDialog.dismiss()
                notify("Xóa thành công")
            } else {
                notify("Phải có ít nhất một ví tiền")
            }
        }
        val btnUpdate = bottomSheetDialog.findViewById<Button>(R.id.btnUpdate)
        btnUpdate!!.setOnClickListener {
            callback.showFragment(requireContext()::class.java,AddWalletFragment::class.java,0,0,wallet,true)
            bottomSheetDialog.dismiss()
        }
    }

}