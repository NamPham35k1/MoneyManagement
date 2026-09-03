package com.screenguard.privacy.screenfilter.ui.fragment.fragment_tool

import com.screenguard.privacy.screenfilter.base.BaseFragment
import com.screenguard.privacy.screenfilter.base.CommonVM
import com.screenguard.privacy.screenfilter.databinding.FragmentPlaceholderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToolFragment : BaseFragment<CommonVM, FragmentPlaceholderBinding>() {
    override fun createViewModel(): Class<CommonVM> = CommonVM::class.java
    override fun getViewDataBinging(): FragmentPlaceholderBinding = FragmentPlaceholderBinding.inflate(layoutInflater)

    override fun initView() {
        binding.tvTitle.text = "Tool Fragment"
    }

    override fun bindViewModel() {
    }
}
