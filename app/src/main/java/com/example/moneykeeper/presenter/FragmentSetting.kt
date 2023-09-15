package com.example.moneykeeper.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentSettingBinding
import com.example.moneykeeper.presenter.base.BaseFragment

class FragmentSetting : BaseFragment<FragmentSettingBinding>() {
    override fun getLayout(container: ViewGroup?): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater)
    }

    override fun initViews() {

    }

}