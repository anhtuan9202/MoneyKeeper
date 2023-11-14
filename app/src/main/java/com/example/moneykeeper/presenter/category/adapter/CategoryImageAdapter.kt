package com.example.moneykeeper.presenter.category.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moneykeeper.databinding.ItemCategoryImageBinding


import com.example.moneykeeper.presenter.interfaces.OnItemClickListener

import com.example.moneykeeper.presenter.utils.ResourceUtils.getDrawableResourceId

import javax.inject.Inject

class CategoryImageAdapter @Inject constructor(
    private val context: Context
) : ListAdapter<String, CategoryImageAdapter.CategoryImageViewHolder>(DiffCallback()) {
    private var itemClickListener: OnItemClickListener? = null
//    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    fun setItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


    inner class CategoryImageViewHolder(private val binding: ItemCategoryImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            binding.ivCategoryImage.setImageResource(context.getDrawableResourceId(image))
//            binding.ivCheck.visibility = if (isSelected) View.VISIBLE else View.GONE
//            if (isSelected) {
//                binding.root.setBackgroundColor(context.resources.getColor(android.R.color.holo_green_light ))
//            } else {
//                binding.root.setBackgroundColor(context.resources.getColor(android.R.color.transparent))
//            }
            binding.root.setOnClickListener {
//                val previousSelectedItemPosition = selectedItemPosition
//                selectedItemPosition = adapterPosition
//
//                notifyItemChanged(selectedItemPosition)
//                if (previousSelectedItemPosition != RecyclerView.NO_POSITION) {
//                    notifyItemChanged(previousSelectedItemPosition)
//                }
                itemClickListener?.onItemClick(image)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemCategoryImageBinding.inflate(inflater, parent, false)
        return CategoryImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryImageViewHolder, position: Int) {
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
