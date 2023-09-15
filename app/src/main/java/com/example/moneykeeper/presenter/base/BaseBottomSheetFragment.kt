package com.example.moneykeeper.presenter.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.example.moneykeeper.presenter.interfaces.OnActionCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<VB : ViewBinding> : BottomSheetDialogFragment() {

    private lateinit var mContext: Context
    protected lateinit var binding: VB
    lateinit var callback: OnActionCallback
    var data: Any? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (context is OnActionCallback) {
            callback = context
        } else {
            throw RuntimeException("$context must implement OnActionCallback")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getLayout(inflater, container)
        return binding.root
    }

    abstract fun getLayout(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()


    }

    abstract fun initViews()


    fun notify(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}