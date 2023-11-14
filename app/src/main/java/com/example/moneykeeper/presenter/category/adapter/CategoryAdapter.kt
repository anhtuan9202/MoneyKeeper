package com.example.moneykeeper.presenter.category.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper.databinding.ItemAddBinding
import com.example.moneykeeper.databinding.ItemCategoryBinding
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.presenter.interfaces.OnDeleteClickListener
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.utils.ResourceUtils.getDrawableResourceId


class CategoryAdapter(private val context: Context) :
    ListAdapter<Any, RecyclerView.ViewHolder>(DiffCallback()) {
    var showTrashIcon = false

    private var itemClickListener: OnItemClickListener? = null
    private var deleteClickListener: OnDeleteClickListener? = null

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_ADD = 1
    }

    fun showTrashIcon(show: Boolean) {
        showTrashIcon = show
        notifyDataSetChanged()

    }

    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    fun setDeleteClickListener(listener: OnDeleteClickListener){
        deleteClickListener = listener
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            if(showTrashIcon){
                binding.ivDelete.visibility = View.VISIBLE

            }
            else {
                binding.ivDelete.visibility = View.GONE

            }

            binding.ivCategoryImageItem.setImageResource(
                context.getDrawableResourceId(category.cateImage))
            binding.tvCategoryNameItem.text = category.cateName

            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(category)
            }
            binding.ivDelete.setOnClickListener {
                deleteClickListener?.onDeleteClick(category)
            }
        }
    }

    inner class AddButtonViewHolder(private val binding: ItemAddBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivAdd.setOnClickListener {
                itemClickListener?.onItemClick("add")
            }
        }
    }



    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item is Category) {
            VIEW_TYPE_CATEGORY
        } else {
            VIEW_TYPE_ADD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> {
                val view = ItemCategoryBinding.inflate(inflater, parent, false)
                CategoryViewHolder(view)
            }
            VIEW_TYPE_ADD -> {
                val view = ItemAddBinding.inflate(inflater, parent, false)
                AddButtonViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is CategoryViewHolder -> holder.bind(item as Category)
            is AddButtonViewHolder -> Unit
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem::class == newItem::class
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }
}
