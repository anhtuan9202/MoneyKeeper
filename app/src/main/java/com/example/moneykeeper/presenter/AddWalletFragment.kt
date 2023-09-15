package com.example.moneykeeper.presenter

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentAddWalletBinding
import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.presenter.base.BaseBottomSheetFragment
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.viewmodel.WalletViewModel
import com.example.moneykeeper.utils.ResourceUtils.getDrawableResourceId
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AddWalletFragment : BaseBottomSheetFragment<FragmentAddWalletBinding>() {
    private lateinit var listImage: List<String>
    private lateinit var walletImage: String
    private lateinit var walletName: String
    private lateinit var walletMoney: String
    @Inject
    lateinit var walletImageAdapter: WalletImageAdapter

    private val walletViewModel: WalletViewModel by viewModels()


    override fun getLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddWalletBinding {
        return FragmentAddWalletBinding.inflate(layoutInflater)
    }

    override fun initViews() {



        binding.ivChooseImage.setOnClickListener {
            binding.elChooseImage.toggle()
        }
        binding.rvWalletImage.layoutManager = GridLayoutManager(requireContext(), 5)
        listImage = listOf<String>(*resources.getStringArray(R.array.img_wallet))
        walletImageAdapter.submitList(listImage)
        binding.rvWalletImage.adapter = walletImageAdapter
        walletImageAdapter.setItemClickListener(object: OnItemClickListener{
            override fun onItemClick(data: Any?) {
                binding.ivChooseImage.setImageResource(requireContext().getDrawableResourceId(data as String))
                walletImage = data
            }
        })

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnSave.setOnClickListener {
            addWallet()

        }
        binding.tvMoney.addTextChangedListener(onTextChangedListener())
        if (arguments != null) {
            walletImage = requireArguments().getString("img").toString()
            requireArguments().getString("img")
                ?.let { it1 -> requireContext().getDrawableResourceId(it1) }
                ?.let { it2 -> binding.ivChooseImage.setImageResource(it2) }
            walletName = requireArguments().getString("name").toString()
            binding.tvWalletName.setText(walletName)
            walletMoney = requireArguments().getString("money").toString()
            binding.tvMoney.setText(walletMoney)
            binding.tvAddWallet.text = "Cập nhật ví tiền"
        }


    }


    private fun addWallet() {
        walletName = binding.tvWalletName.text.toString().trim()
        walletMoney = binding.tvMoney.text.toString().replace(",", "")
        if (TextUtils.isEmpty(walletName)) {
            notify("Tên ví tiền không được để trống!")
            return
        }
        if (TextUtils.isEmpty(walletMoney)) {
            notify("Số tiền không được để trống!")
            return
        }
        if (TextUtils.isEmpty(walletImage)) {
            walletImage = "wallet_cash"
        }
        if (arguments == null) {
            val wallet = Wallet( walImage = walletImage, walName = walletName, walMoney = walletMoney)
            walletViewModel.insertWallet(wallet)
            notify("Thêm ví tiền thành công!")
        } else {
            val wallet = Wallet(requireArguments().getInt("id"), walImage = walletImage, walName = walletName, walMoney = walletMoney)
            walletViewModel.updateWallet(wallet)
            notify("Cập nhật ví tiền thành công!")
        }
        dismiss()

    }
    fun newInstance(wallet: Wallet): AddWalletFragment {
        val dialog = AddWalletFragment()
        val args = Bundle()
        args.putInt("id", wallet.walId)
        args.putString("name", wallet.walName)
        args.putString("img", wallet.walImage)
        args.putString("money", wallet.walMoney)
        dialog.arguments = args
        return dialog
    }

    fun newInstance(): AddWalletFragment {
        return AddWalletFragment()
    }


    private fun onTextChangedListener(): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding.tvMoney.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    val longval: Long = originalString.toLong()
                    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longval)

                    //setting text after format to EditText
                    binding.tvMoney.setText(formattedString)
                    binding.tvMoney.text?.let { binding.tvMoney.setSelection(it.length) }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                binding.tvMoney.addTextChangedListener(this)
            }
        }
    }

}