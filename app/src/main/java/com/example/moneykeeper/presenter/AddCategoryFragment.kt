package com.example.moneykeeper.presenter

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentAddCategoryBinding
import com.example.moneykeeper.domain.model.Category
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.interfaces.OnItemClickListener
import com.example.moneykeeper.presenter.viewmodel.CategoryViewModel
import com.example.moneykeeper.utils.ResourceUtils.getDrawableResourceId
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddCategoryFragment : BaseFragment<FragmentAddCategoryBinding>() {
    private lateinit var listCategory: List<String>
    private var categoryImage: String? = null
    private lateinit var categoryName: String
    private var categoryType: Int = 1;
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    @Inject
    lateinit var adapter: CategoryImageAdapter
    override fun getLayout(container: ViewGroup?): FragmentAddCategoryBinding {
        return FragmentAddCategoryBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        listCategory = listOf<String>(*resources.getStringArray(R.array.img_cat))
        binding.rvCategory.layoutManager = GridLayoutManager(requireContext(),4)
        binding.rvCategory.adapter = adapter
        adapter.submitList(listCategory)
        binding.ivChooseImage.setOnClickListener {
            binding.elCategory.toggle()
        }

        binding.tgbCategory.addOnButtonCheckedListener(MaterialButtonToggleGroup.OnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                R.id.btnExpense -> {
                    updateButtonUI(isRevenue = false)
                    categoryType = 2
                }

                R.id.btnRevenue -> {
                    updateButtonUI(isRevenue = true)
                    categoryType = 1
                }
            }
        })
        adapter.setItemClickListener(object: OnItemClickListener {
            override fun onItemClick(data: Any?) {
                categoryImage = data as String
                binding.ivChooseImage.setImageResource(requireContext().getDrawableResourceId(data))

            }
        })
        binding.btnSave.setOnClickListener {
            handleSave()
        }
        binding.ivBack.setOnClickListener {
            callback.showFragment(this::class.java,CategoryFragment::class.java,0,0,null)
        }

    }
    private fun updateButtonUI(isRevenue: Boolean) {
        val revenueColor = if (isRevenue) R.color.button_checked else R.color.button_uncheck
        val expenseColor = if (!isRevenue) R.color.button_checked else R.color.button_uncheck

        binding.btnRevenue.setBackgroundColor(resources.getColor(revenueColor))
        binding.btnRevenue.setTextColor(resources.getColor(if (isRevenue) R.color.white else R.color.black))
        binding.btnExpense.setBackgroundColor(resources.getColor(expenseColor))
        binding.btnExpense.setTextColor(resources.getColor(if (!isRevenue) R.color.white else R.color.black))
    }
    private fun handleSave() {
        categoryName = binding.etCategory.text.toString().trim()
        if (TextUtils.isEmpty(categoryImage)) {
            notify("Bạn chưa chọn biểu tượng")
            return
        }
        if (TextUtils.isEmpty(categoryName)) {
            notify("Bạn chưa nhập tên danh mục")
            return

        }
        val category =
            categoryImage?.let { Category(cateName = categoryName, cateImage = it, cateType = categoryType) }
        if (category != null) {
            categoryViewModel.insertCategory(category)
        }
        notify("Thêm danh mục thành công")
        callback.showFragment(this::class.java,CategoryFragment::class.java,0,0,null)
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<BottomAppBar>(R.id.bottomAppBar)?.visibility = View.INVISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.fAB)?.visibility = View.INVISIBLE
    }
}