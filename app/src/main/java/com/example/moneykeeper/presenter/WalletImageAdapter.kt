package com.example.moneykeeper.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper.databinding.ItemWalletImageBinding


import com.example.moneykeeper.presenter.interfaces.OnItemClickListener

import com.example.moneykeeper.utils.ResourceUtils.getDrawableResourceId

import javax.inject.Inject

class WalletImageAdapter @Inject constructor(
    private val context: Context
) : ListAdapter<String, WalletImageAdapter.WalletImageViewHolder>(DiffCallback()) {
    private var itemClickListener: OnItemClickListener? = null
    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


    inner class WalletImageViewHolder(private val binding: ItemWalletImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String) {
            binding.ivWalletImage.setImageResource(context.getDrawableResourceId(image))


            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(image)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemWalletImageBinding.inflate(inflater, parent, false)
        return WalletImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalletImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem== newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
