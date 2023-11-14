package com.example.moneykeeper.presenter.expense.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.ItemExpenseBinding

import com.example.moneykeeper.domain.model.Expense
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.category.viewmodel.CategoryViewModel
import com.example.moneykeeper.presenter.wallet.viewmodel.WalletViewModel
import com.example.moneykeeper.presenter.utils.DateUtils
import com.example.moneykeeper.presenter.utils.NumberFormatter

import com.example.moneykeeper.presenter.utils.ResourceUtils.getDrawableResourceId
import kotlinx.coroutines.*
import javax.inject.Inject

class ExpenseAdapter @Inject constructor(
    private val categoryViewModel: CategoryViewModel,
    private val walletViewModel: WalletViewModel,
    private val context: Context
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {
    private var itemClickListener: OnItemClickListener? = null
    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


    inner class ExpenseViewHolder(private val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            binding.tvNote.text = expense.expNote
            binding.tvMoney.text = NumberFormatter.formatNumber(expense.expMoney)
            CoroutineScope(Dispatchers.IO).launch {
                val category = categoryViewModel.getCategoryById(expense.expCategory)

                withContext(Dispatchers.Main) {
                    binding.ivCategory.setImageResource(context.getDrawableResourceId(category.cateImage))
                    binding.tvCategory.text = category.cateName
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                val wallet = walletViewModel.getWalletById(expense.expWallet)
                if(wallet != null){
                    withContext(Dispatchers.Main) {
                        binding.ivExpWallet.setImageResource(context.getDrawableResourceId(wallet.walImage))
                    }
                }

            }
            
            if(expense.expNote.isNotEmpty()){
                binding.tvNote.visibility = View.VISIBLE
                binding.tvNote.text = expense.expNote
            }
            else {
                val layoutParams = binding.tvCategory.layoutParams as RelativeLayout.LayoutParams
                binding.tvNote.visibility = View.GONE
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1)
                binding.tvCategory.layoutParams = layoutParams
            }

            binding.tvMoney.text = expense.expMoney
            if(expense.expType == 1){
                binding.tvMoney.setTextColor(context.resources.getColor(R.color.button_success))
                binding.ivUpDown.setImageResource(R.drawable.ic_up)
                binding.ivUpDown.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.button_success
                    )
                )
            } else {
                binding.tvMoney.setTextColor(context.resources.getColor(R.color.button_cancel))
                binding.ivUpDown.setImageResource(R.drawable.ic_down)
                binding.ivUpDown.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.button_cancel
                    )
                )
            }
            binding.tvDate.text = DateUtils.formatDate(expense.expDate)
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(expense)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemExpenseBinding.inflate(inflater, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    fun getItemView(recyclerView: RecyclerView, expense: Expense): View? {
        val position = currentList.indexOf(expense)
        return if (position != RecyclerView.NO_POSITION) {
            recyclerView.layoutManager?.findViewByPosition(position)
        } else {
            null
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.expId == newItem.expId
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}
