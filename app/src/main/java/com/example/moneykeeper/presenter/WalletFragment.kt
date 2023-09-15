package com.example.moneykeeper.presenter

import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentWalletBinding
import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.viewmodel.ExpenseViewModel
import com.example.moneykeeper.presenter.viewmodel.WalletViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class WalletFragment : BaseFragment<FragmentWalletBinding>() {
    private var walletCount: Int = 0
    private lateinit var wallet: Wallet
    private val walletViewModel: WalletViewModel by viewModels()
    private val expenseViewModel: ExpenseViewModel by viewModels()
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
            showBottomSheetAddWallet()
        }

        walletAdapter.setItemClickListener(object: com.example.moneykeeper.presenter.interfaces.OnItemClickListener{
            override fun onItemClick(data: Any?) {
                if(data != null){
                    wallet = data as Wallet
                    showBottomSheetDeleteUpdate()
                }

            }

        })
    }

    private fun showBottomSheetAddWallet() {
        val addWalletFragment = AddWalletFragment().newInstance()
        addWalletFragment.show(requireActivity().supportFragmentManager, "Add Wallet")
    }

    private fun showBottomSheetDeleteUpdate(){
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.bottomsheet_menu_delete_update)
        bottomSheetDialog.show()
        val btnDelete = bottomSheetDialog.findViewById<Button>(R.id.btnDelete)
        btnDelete!!.setOnClickListener {
            if (walletCount > 1) {
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
            val addWalletFragment: AddWalletFragment = AddWalletFragment().newInstance(wallet)
                addWalletFragment.show(requireActivity().supportFragmentManager, "Add Wallet")
            bottomSheetDialog.dismiss()
        }
    }

}