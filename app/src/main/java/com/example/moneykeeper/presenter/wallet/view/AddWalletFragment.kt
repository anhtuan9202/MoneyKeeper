package com.example.moneykeeper.presenter.wallet.view

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentAddWalletBinding
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.presenter.wallet.adapter.WalletImageAdapter
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.wallet.viewmodel.WalletViewModel
import com.example.moneykeeper.presenter.utils.NumberFormatter
import com.example.moneykeeper.presenter.utils.ResourceUtils.getDrawableResourceId
import com.example.moneykeeper.presenter.utils.TextChanged
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddWalletFragment : BaseFragment<FragmentAddWalletBinding>() {
    private lateinit var listImage: List<String>
    private  var walletImage: String = ""
    private lateinit var walletName: String
    private lateinit var walletMoney: String
    private var isEdit = false
    @Inject
    lateinit var adapter: WalletImageAdapter

    private val walletViewModel: WalletViewModel by activityViewModels()


    override fun getLayout(container: ViewGroup?): FragmentAddWalletBinding {
        return FragmentAddWalletBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        if(data != null && data is Wallet){
            isEdit = true
            walletImage = (data as Wallet).walImage
        }
        if(isEdit){
            binding.tvTitle.text = "Cập nhật ví tiền"
            binding.etWalletName.setText((data as Wallet).walName)
            binding.etWalletMoney.setText(NumberFormatter.formatNumber((data as Wallet).walMoney))
//            adapter.selected((data as Wallet).walImage)
            binding.ivChooseImage.setImageResource(requireContext().getDrawableResourceId((data as Wallet).walImage))
        }
        binding.ivChooseImage.setOnClickListener {
            binding.elWallet.toggle()
        }
        listImage = listOf<String>(*resources.getStringArray(R.array.img_wallet))
        binding.rvWallet.layoutManager = GridLayoutManager(requireContext(),4)
        binding.rvWallet.adapter = adapter
        adapter.submitList(listImage)

        adapter.setItemClickListener(object: OnItemClickListener {
            override fun onItemClick(data: Any?) {
                walletImage = data as String
                binding.ivChooseImage.setImageResource(requireContext().getDrawableResourceId(data))

            }
        })
        binding.ivBack.setOnClickListener {
            callback.backToPrevious()
        }
        binding.btnSave.setOnClickListener {
            handleSave()
        }
        binding.etWalletMoney.addTextChangedListener(TextChanged.onTextChangedListener(binding.etWalletMoney))




    }

    private fun handleSave() {
        walletName = binding.etWalletName.text.toString().trim()
        walletMoney = binding.etWalletMoney.text.toString().replace(",", "")
        if (TextUtils.isEmpty(walletName)) {
            notify(getString(R.string.wallet_name_blank))
            return
        }
        if (TextUtils.isEmpty(walletMoney)) {
            notify(getString(R.string.amount_blank))
            return
        }
        if (TextUtils.isEmpty(walletImage)) {
            notify(getString(R.string.icon_blank))
            return

        }
        if(isEdit){
            val wallet = Wallet((data as Wallet).walId, walImage = walletImage, walName = walletName, walMoney = walletMoney)
            walletViewModel.updateWallet(wallet)
            callback.backToPrevious()
            notify(getString(R.string.update_wallet_succ))
        }
        else {
            val wallet = Wallet( walImage = walletImage, walName = walletName, walMoney = walletMoney)
            walletViewModel.insertWallet(wallet)
            callback.backToPrevious()
            notify(getString(R.string.add_wallet_succ))

        }

    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clayout)?.visibility = View.INVISIBLE
        activity?.findViewById<AdView>(R.id.adView)?.visibility = View.INVISIBLE

    }
}