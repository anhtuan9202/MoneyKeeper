package com.example.moneykeeper.presenter.budget.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper.databinding.ItemBudgetBinding
import com.example.moneykeeper.domain.model.Budget
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.viewmodel.CategoryViewModel
import com.example.moneykeeper.presenter.expense.viewmodel.ExpenseViewModel
import com.example.moneykeeper.utils.ResourceUtils.getDrawableResourceId
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class BudgetAdapter @Inject constructor(
    private val categoryViewModel: CategoryViewModel,
    private val expenseViewModel: ExpenseViewModel,
    private val context: Context
) : ListAdapter<Budget, BudgetAdapter.BudgetViewHolder>(DiffCallback()) {
    private var itemClickListener: OnItemClickListener? = null
    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    inner class BudgetViewHolder(private val binding: ItemBudgetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(budget: Budget) {
            CoroutineScope(Dispatchers.IO).launch {
                val category = categoryViewModel.getCategoryById(budget.budCategory)

                withContext(Dispatchers.Main) {
                    binding.ivCategory.setImageResource(context.getDrawableResourceId(category.cateImage))
                    binding.tvCategory.text = category.cateName
                }
            }
            val dateFormat = SimpleDateFormat("MM/yyyy",Locale.US)
            val formattedDate = dateFormat.format(budget.budMonth)
            binding.tvMonth.text = formattedDate
            binding.tvMoney.text = budget.budMoney
            binding.tvPbBudget.text = budget.budMoney
            expenseViewModel.calculate(budget.budCategory, dateFormat.format(budget.budMonth)) {
                val progressValue = (it.toFloat() / budget.budMoney.toFloat() * 100).toInt()
                binding.pbBudget.progress = progressValue
                binding.pbBudget.show()
                binding.tvPbPercent.text = String.format("%d%%", progressValue)
                binding.tvPbMoney.text = it.toString()
            }
            binding.root.setOnClickListener{
                itemClickListener?.onItemClick(budget)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemBudgetBinding.inflate(inflater, parent, false)
        return BudgetViewHolder(view)
    }
    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class DiffCallback : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.budId == newItem.budId
        }
        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem == newItem
        }
    }
}
