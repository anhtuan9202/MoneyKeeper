package com.example.moneykeeper.presenter.wallet.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneykeeper.databinding.FragmentChooseWalletBinding
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.presenter.base.BaseBottomSheetFragment
import com.example.moneykeeper.presenter.interfaces.ChooseWalletListener
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.wallet.viewmodel.WalletViewModel
import com.example.moneykeeper.presenter.wallet.adapter.WalletAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChooseWalletFragment : BaseBottomSheetFragment<FragmentChooseWalletBinding>(){
    @Inject
    lateinit var adapter: WalletAdapter

    private val walletViewModel: WalletViewModel by activityViewModels()

    private lateinit var listener: ChooseWalletListener

    fun setDialogFragmentListener(listener: ChooseWalletListener) {
        this.listener = listener
    }
    companion object {
        fun newInstance(): ChooseWalletFragment {
            return ChooseWalletFragment()
        }
    }

    override fun getLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChooseWalletBinding {
        return FragmentChooseWalletBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        binding.rvChooseWallet.layoutManager = LinearLayoutManager(context)
        binding.rvChooseWallet.adapter = adapter

        adapter.setItemClickListener(object: OnItemClickListener{
            override fun onItemClick(data: Any?) {
                val wallet = data as Wallet
                listener.onChooseListener(wallet)
                dismiss()
            }

        })


        walletViewModel.getWallets()
        walletViewModel.walletsLiveData.observe(this){
            adapter.submitList(it)
        }
    }
}