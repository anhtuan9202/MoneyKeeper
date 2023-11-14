package com.example.moneykeeper.presenter.wallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper.databinding.ItemWalletBinding

import com.example.moneykeeper.domain.model.Wallet
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.utils.NumberFormatter

import com.example.moneykeeper.presenter.utils.ResourceUtils.getDrawableResourceId
import javax.inject.Inject

class WalletAdapter @Inject constructor(
    private val context: Context
) : ListAdapter<Wallet, WalletAdapter.WalletViewHolder>(DiffCallback()) {
    private var itemClickListener: OnItemClickListener? = null
    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


    inner class WalletViewHolder(private val binding: ItemWalletBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(wallet: Wallet) {
            binding.ivWalletImage.setImageResource(context.getDrawableResourceId(wallet.walImage))
            binding.tvItemWalletMoney.text = NumberFormatter.formatNumber(wallet.walMoney)
            binding.tvItemWalletName.text = wallet.walName

            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(wallet)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemWalletBinding.inflate(inflater, parent, false)
        return WalletViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<Wallet>() {
        override fun areItemsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
            return oldItem.walId == newItem.walId
        }

        override fun areContentsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
            return oldItem == newItem
        }
    }
}
